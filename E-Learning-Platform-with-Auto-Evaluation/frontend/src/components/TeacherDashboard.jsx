import React, { useState, useEffect } from 'react';

const TeacherDashboard = ({ onLogout }) => {
  const [activeTab, setActiveTab] = useState('create');
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (activeTab === 'view') {
      fetchAssignments();
    }
  }, [activeTab]);

  const fetchAssignments = async () => {
    setLoading(true);
    try {
      const response = await fetch('/admin/assignments');
      if (response.ok) {
        const data = await response.json();
        setAssignments(data.assignments || []);
      }
    } catch (err) {
      console.error('Failed to fetch assignments:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-gray-50 to-white">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 sticky top-0 z-40 shadow-sm">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            {/* Logo */}
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 bg-gradient-to-br from-blue-600 via-indigo-600 to-purple-600 rounded-xl flex items-center justify-center shadow-lg">
                <span className="text-white text-base font-bold">AS</span>
              </div>
              <div>
                <h1 className="text-xl font-bold text-gray-900">Atlas School</h1>
                <p className="text-xs text-gray-500">Teacher Portal</p>
              </div>
            </div>

            {/* Logout */}
            <button
              onClick={onLogout}
              className="px-4 py-2 text-sm font-medium text-gray-700 hover:text-gray-900 border border-gray-300 rounded-lg hover:bg-gray-50 transition-all"
            >
              Logout
            </button>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-6 py-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-2">
            Welcome, Teacher!
          </h2>
          <p className="text-gray-600">
            Create and manage assignments for your students
          </p>
        </div>

        {/* Tabs */}
        <div className="flex gap-4 mb-8 border-b border-gray-200">
          <button
            onClick={() => setActiveTab('create')}
            className={`px-6 py-3 font-semibold transition-all relative ${
              activeTab === 'create'
                ? 'text-blue-600'
                : 'text-gray-600 hover:text-gray-900'
            }`}
          >
            Create Assignment
            {activeTab === 'create' && (
              <div className="absolute bottom-0 left-0 right-0 h-0.5 bg-blue-600"></div>
            )}
          </button>
          <button
            onClick={() => setActiveTab('view')}
            className={`px-6 py-3 font-semibold transition-all relative ${
              activeTab === 'view'
                ? 'text-blue-600'
                : 'text-gray-600 hover:text-gray-900'
            }`}
          >
            View Assignments
            {activeTab === 'view' && (
              <div className="absolute bottom-0 left-0 right-0 h-0.5 bg-blue-600"></div>
            )}
          </button>
        </div>

        {/* Tab Content */}
        {activeTab === 'create' ? (
          <CreateAssignmentSection onSuccess={() => setActiveTab('view')} />
        ) : (
          <ViewAssignmentsSection
            assignments={assignments}
            loading={loading}
            onRefresh={fetchAssignments}
          />
        )}
      </main>
    </div>
  );
};

const CreateAssignmentSection = ({ onSuccess }) => {
  const [file, setFile] = useState(null);
  const [teacherName, setTeacherName] = useState('');
  const [assignmentType, setAssignmentType] = useState('TEXT');
  const [testFile, setTestFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      setFile(selectedFile);
      setError('');
    }
  };

  const handleTestFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      setTestFile(selectedFile);
      setError('');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!teacherName.trim()) {
      setError('Please enter teacher name');
      return;
    }

    if (assignmentType === 'TEXT' && !file) {
      setError('Please select a questions file for text assignment');
      return;
    }

    if (assignmentType === 'CODE' && !testFile) {
      setError('Please select a test cases file for code assignment');
      return;
    }

    setUploading(true);
    setError('');
    setSuccess('');

    try {
      const formData = new FormData();
      formData.append('assignmentType', assignmentType);
      formData.append('teacherName', teacherName);

      if (assignmentType === 'CODE') {
        formData.append('testFile', testFile);
      } else {
        formData.append('assignmentFile', file);
      }

      const response = await fetch('/admin', {
        method: 'POST',
        body: formData,
      });

      if (response.ok) {
        const data = await response.json();
        const fileStoredMsg = data.fileStored ? ' File stored in cloud storage' : '';
        const assignmentTypeMsg = assignmentType === 'CODE' ? ' (Code Assignment)' : '';
        setSuccess(`Assignment created successfully! ID: ${data.assignmentId}${assignmentTypeMsg}${fileStoredMsg}`);
        setFile(null);
        setTestFile(null);
        setTeacherName('');
        setAssignmentType('TEXT');
        setTimeout(() => {
          onSuccess();
        }, 2000);
      } else {
        const errorData = await response.json();
        setError(errorData.error || 'Failed to create assignment');
      }
    } catch (err) {
      setError('Connection error. Please check if backend is running.');
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto">
      <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
        <h3 className="text-2xl font-bold text-gray-900 mb-6">Create New Assignment</h3>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Teacher Name */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              Teacher Name
            </label>
            <input
              type="text"
              value={teacherName}
              onChange={(e) => setTeacherName(e.target.value)}
              placeholder="Enter your name"
              className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              disabled={uploading}
            />
          </div>

          {/* Assignment Type */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              Assignment Type
            </label>
            <div className="flex gap-4">
              <label className="flex items-center space-x-2 cursor-pointer">
                <input
                  type="radio"
                  value="TEXT"
                  checked={assignmentType === 'TEXT'}
                  onChange={(e) => setAssignmentType(e.target.value)}
                  disabled={uploading}
                  className="w-4 h-4 text-blue-600"
                />
                <span className="text-sm">Text Assignment (Questions & Answers)</span>
              </label>
              <label className="flex items-center space-x-2 cursor-pointer">
                <input
                  type="radio"
                  value="CODE"
                  checked={assignmentType === 'CODE'}
                  onChange={(e) => setAssignmentType(e.target.value)}
                  disabled={uploading}
                  className="w-4 h-4 text-blue-600"
                />
                <span className="text-sm">Code Assignment (Auto-graded with tests)</span>
              </label>
            </div>
          </div>

          {/* File Upload - Only show for TEXT assignments */}
          {assignmentType === 'TEXT' && (
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-2">
                Questions File (Required)
              </label>
              <div className="border-2 border-dashed border-gray-300 rounded-xl p-8 text-center hover:border-blue-400 transition-all">
                <input
                  type="file"
                  onChange={handleFileChange}
                  accept=".txt"
                  className="hidden"
                  id="file-upload-teacher"
                  disabled={uploading}
                />
                <label
                  htmlFor="file-upload-teacher"
                  className="cursor-pointer flex flex-col items-center"
                >
                  <svg className="w-12 h-12 text-gray-400 mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                  </svg>
                  {file ? (
                    <p className="text-sm font-medium text-gray-900">{file.name}</p>
                  ) : (
                    <>
                      <p className="text-sm font-medium text-gray-900">Click to upload questions</p>
                      <p className="text-xs text-gray-500 mt-1">TXT file with questions and answers</p>
                    </>
                  )}
                </label>
              </div>
            </div>
          )}

          {/* Test File Upload (for CODE assignments) */}
          {assignmentType === 'CODE' && (
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-2">
                Test Cases File (Required)
              </label>
              <div className="border-2 border-dashed border-orange-300 rounded-xl p-8 text-center hover:border-orange-400 transition-all">
                <input
                  type="file"
                  onChange={handleTestFileChange}
                  accept=".txt"
                  className="hidden"
                  id="test-file-upload"
                  disabled={uploading}
                />
                <label
                  htmlFor="test-file-upload"
                  className="cursor-pointer flex flex-col items-center"
                >
                  <svg className="w-12 h-12 text-orange-400 mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                  </svg>
                  {testFile ? (
                    <p className="text-sm font-medium text-gray-900">{testFile.name}</p>
                  ) : (
                    <>
                      <p className="text-sm font-medium text-gray-900">Click to upload test cases</p>
                      <p className="text-xs text-gray-500 mt-1">TXT file with test inputs and expected outputs</p>
                    </>
                  )}
                </label>
              </div>
            </div>
          )}

          {/* Error Message */}
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-xl text-sm">
              {error}
            </div>
          )}

          {/* Success Message */}
          {success && (
            <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-xl text-sm">
              {success}
            </div>
          )}

          {/* Submit Button */}
          <button
            type="submit"
            disabled={uploading}
            className="w-full bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-semibold py-4 rounded-xl shadow-lg hover:shadow-xl transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {uploading ? 'Creating Assignment...' : 'Create Assignment'}
          </button>
        </form>
      </div>
    </div>
  );
};

const ViewAssignmentsSection = ({ assignments, loading, onRefresh }) => {
  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between mb-6">
        <h3 className="text-xl font-bold text-gray-900">All Assignments</h3>
        <button
          onClick={onRefresh}
          className="px-4 py-2 text-sm font-medium text-blue-600 hover:text-blue-700 border border-blue-300 rounded-lg hover:bg-blue-50 transition-all"
        >
          Refresh
        </button>
      </div>

      {assignments.length > 0 ? (
        assignments.map((assignment, index) => (
          <div
            key={index}
            className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow"
          >
            <div className="flex items-start justify-between">
              <div className="flex-1">
                <div className="flex items-center gap-2 mb-2">
                  <h4 className="font-semibold text-gray-900 text-lg">
                    {assignment.title || 'Untitled Assignment'}
                  </h4>
                  {assignment.assignmentType === 'CODE' && (
                    <span className="px-2 py-1 bg-purple-100 text-purple-700 text-xs font-semibold rounded-lg">
                       CODE
                    </span>
                  )}
                </div>
                <p className="text-sm text-gray-600 mb-3">
                  {assignment.description || 'No description'}
                </p>
                <div className="flex items-center gap-4 text-xs text-gray-500">
                  <span className="flex items-center gap-1">
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                    {assignment.teacherName}
                  </span>
                  {assignment.assignmentType === 'CODE' ? (
                    <span className="flex items-center gap-1">
                      <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
                      </svg>
                      {assignment.testCaseCount || 0} test cases
                    </span>
                  ) : (
                    <span className="flex items-center gap-1">
                      <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                      </svg>
                      {assignment.questionCount || 0} questions
                    </span>
                  )}
                  <span className="flex items-center gap-1">
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z" />
                    </svg>
                    {assignment.totalPoints || 0} points
                  </span>
                  {assignment.s3Url && (
                    <span className="flex items-center gap-1 text-green-600">
                      <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 15a4 4 0 004 4h9a5 5 0 10-.1-9.999 5.002 5.002 0 10-9.78 2.096A4.001 4.001 0 003 15z" />
                      </svg>
                      Stored in S3
                    </span>
                  )}
                </div>
              </div>
              <div className="ml-6 text-center bg-blue-50 rounded-lg px-4 py-2">
                <p className="text-xs text-gray-600 mb-1">Assignment ID</p>
                <p className="text-sm font-mono font-bold text-blue-600">{assignment.assignmentId}</p>
              </div>
            </div>
          </div>
        ))
      ) : (
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-12 text-center">
          <svg className="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
          <p className="text-gray-500">No assignments yet. Create your first assignment!</p>
        </div>
      )}
    </div>
  );
};

export default TeacherDashboard;
