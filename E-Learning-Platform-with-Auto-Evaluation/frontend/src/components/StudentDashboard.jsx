import React, { useState } from 'react';

const StudentDashboard = ({ studentData, onLogout }) => {
  const [activeTab, setActiveTab] = useState('assignments'); 

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
                <p className="text-xs text-gray-500">Student Portal</p>
              </div>
            </div>

            {/* User Info & Logout */}
            <div className="flex items-center gap-4">
              <div className="hidden md:block text-right">
                <p className="text-sm font-semibold text-gray-900">{studentData.name}</p>
                <p className="text-xs text-gray-500">{studentData.studentId}</p>
              </div>
              <button
                onClick={onLogout}
                className="px-4 py-2 text-sm font-medium text-gray-700 hover:text-gray-900 border border-gray-300 rounded-lg hover:bg-gray-50 transition-all"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-6 py-8">
        {/* Welcome Section */}
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-gray-900 mb-2">
            Welcome back, {studentData.name}! 
          </h2>
        </div>

        {/* Tabs */}
        <div className="flex gap-4 mb-8 border-b border-gray-200">
          <button
            onClick={() => setActiveTab('assignments')}
            className={`px-6 py-3 font-semibold transition-all relative ${
              activeTab === 'assignments'
                ? 'text-blue-600'
                : 'text-gray-600 hover:text-gray-900'
            }`}
          >
            Upload Assignment
            {activeTab === 'assignments' && (
              <div className="absolute bottom-0 left-0 right-0 h-0.5 bg-blue-600"></div>
            )}
          </button>
          <button
            onClick={() => setActiveTab('scores')}
            className={`px-6 py-3 font-semibold transition-all relative ${
              activeTab === 'scores'
                ? 'text-blue-600'
                : 'text-gray-600 hover:text-gray-900'
            }`}
          >
            My Scores
            {activeTab === 'scores' && (
              <div className="absolute bottom-0 left-0 right-0 h-0.5 bg-blue-600"></div>
            )}
          </button>
        </div>

        {/* Tab Content */}
        {activeTab === 'assignments' ? (
          <AssignmentUploadSection studentData={studentData} />
        ) : (
          <ScoresSection studentData={studentData} />
        )}
      </main>
    </div>
  );
};

const AssignmentUploadSection = ({ studentData }) => {
  const [assignmentId, setAssignmentId] = useState('');
  const [evaluationMode, setEvaluationMode] = useState('ai');
  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [assignments, setAssignments] = useState([]);
  const [loadingAssignments, setLoadingAssignments] = useState(true);
  const [evaluationStatus, setEvaluationStatus] = useState('');
  const [selectedAssignment, setSelectedAssignment] = useState(null);

  React.useEffect(() => {
    fetchAssignments();
  }, []);

  const fetchAssignments = async () => {
    try {
      const response = await fetch('http://localhost:8080/admin/assignments');
      if (response.ok) {
        const data = await response.json();
        setAssignments(data.assignments || []);
      }
    } catch (err) {
      console.error('Failed to fetch assignments:', err);
    } finally {
      setLoadingAssignments(false);
    }
  };

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      if (selectedFile.size > 10 * 1024 * 1024) {
        setError('File size must be less than 10MB');
        return;
      }

      const isCodeAssignment = selectedAssignment?.assignmentType === 'CODE';
      if (isCodeAssignment) {
        const fileName = selectedFile.name.toLowerCase();
        if (!fileName.endsWith('.java') && !fileName.endsWith('.zip')) {
          setError('Please upload a .java or .zip file for code assignments');
          return;
        }
        setEvaluationStatus(`✓ ${selectedFile.name} selected - Ready to compile and test`);
      }

      setFile(selectedFile);
      setError('');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!file) {
      setError('Please select a file to upload');
      return;
    }

    setUploading(true);
    setError('');
    setResult(null);
    setEvaluationStatus('');

    const isCodeAssignment = selectedAssignment?.assignmentType === 'CODE';

    try {
      if (isCodeAssignment) {
        setEvaluationStatus('Uploading and compiling your code...');
      }

      const formData = new FormData();
      formData.append('assignmentFile', file);
      formData.append('studentId', studentData.studentId);
      formData.append('studentName', studentData.name);
      formData.append('assignmentId', assignmentId);
      formData.append('evaluationMode', evaluationMode);

      const response = await fetch('http://localhost:8080/upload', {
        method: 'POST',
        body: formData,
      });

      if (response.ok) {
        const data = await response.json();

        if (isCodeAssignment) {
          setEvaluationStatus('Code evaluation complete!');
        }

        setResult(data);
        setFile(null);
        setAssignmentId('');
        setSelectedAssignment(null);
      } else {
        const errorData = await response.json();
        setError(errorData.error || 'Upload failed. Please try again.');
        setEvaluationStatus('');
      }
    } catch (err) {
      setError('Connection error. Please check if backend is running.');
      setEvaluationStatus('');
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
      {/* Upload Form */}
      <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6">
        <h3 className="text-xl font-bold text-gray-900 mb-6">Upload Assignment</h3>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Assignment Dropdown */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              Select Assignment <span className="text-gray-400 font-normal">(Optional)</span>
            </label>
            {loadingAssignments ? (
              <div className="w-full px-4 py-3 border border-gray-300 rounded-xl bg-gray-50 text-gray-500">
                Loading assignments...
              </div>
            ) : assignments.length > 0 ? (
              <select
                value={assignmentId}
                onChange={(e) => {
                  setAssignmentId(e.target.value);
                  const selected = assignments.find(a => a.assignmentId === e.target.value);
                  setSelectedAssignment(selected || null);
                  setFile(null);
                  setEvaluationStatus('');
                }}
                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
              >
                <option value="">-- Select an assignment or leave empty --</option>
                {assignments.map((assignment) => (
                  <option key={assignment.assignmentId} value={assignment.assignmentId}>
                    {assignment.assignmentType === 'CODE' ? '💻 ' : '📝 '}
                    {assignment.title || 'Untitled'} - {assignment.assignmentType === 'CODE' ? `${assignment.testCaseCount || 0} tests` : `${assignment.questionCount || 0} questions`} ({assignment.totalPoints || 0} pts)
                  </option>
                ))}
              </select>
            ) : (
              <div className="w-full px-4 py-3 border border-gray-300 rounded-xl bg-gray-50 text-gray-500">
                No assignments available. Teacher needs to create assignments first.
              </div>
            )}
            <p className="mt-1 text-xs text-gray-500">
              Leave empty for blind AI evaluation without reference
            </p>
          </div>

          {/* Evaluation Mode */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              Evaluation Mode
            </label>
            <div className="grid grid-cols-2 gap-3">
              <button
                type="button"
                onClick={() => setEvaluationMode('ai')}
                className={`px-4 py-3 rounded-xl border-2 transition-all ${
                  evaluationMode === 'ai'
                    ? 'border-blue-600 bg-blue-50 text-blue-700'
                    : 'border-gray-200 bg-white text-gray-700 hover:border-gray-300'
                }`}
              >
                <div className="text-center">
                  <p className="font-semibold">AI</p>
                  <p className="text-xs mt-1">Intelligent</p>
                </div>
              </button>
              <button
                type="button"
                onClick={() => setEvaluationMode('manual')}
                className={`px-4 py-3 rounded-xl border-2 transition-all ${
                  evaluationMode === 'manual'
                    ? 'border-blue-600 bg-blue-50 text-blue-700'
                    : 'border-gray-200 bg-white text-gray-700 hover:border-gray-300'
                }`}
              >
                <div className="text-center">
                  <p className="font-semibold">Manual</p>
                  <p className="text-xs mt-1">Keyword-based</p>
                </div>
              </button>
            </div>
          </div>

          {/* File Upload */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              {selectedAssignment?.assignmentType === 'CODE' ? 'Code File (.java or .zip)' : 'Assignment File'}
            </label>
            <div className={`border-2 border-dashed rounded-xl p-6 text-center transition-all ${
              selectedAssignment?.assignmentType === 'CODE'
                ? 'border-purple-300 hover:border-purple-400'
                : 'border-gray-300 hover:border-blue-400'
            }`}>
              <input
                type="file"
                onChange={handleFileChange}
                accept={selectedAssignment?.assignmentType === 'CODE' ? '.java,.zip' : '.pdf,.doc,.docx,.txt,.zip'}
                className="hidden"
                id="file-upload"
                disabled={uploading}
              />
              <label
                htmlFor="file-upload"
                className="cursor-pointer flex flex-col items-center"
              >
                {selectedAssignment?.assignmentType === 'CODE' ? (
                  <svg className="w-12 h-12 text-purple-400 mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4" />
                  </svg>
                ) : (
                  <svg className="w-12 h-12 text-gray-400 mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                  </svg>
                )}
                {file ? (
                  <div>
                    <p className="text-sm font-medium text-gray-900">{file.name}</p>
                    {selectedAssignment?.assignmentType === 'CODE' && (
                      <p className="text-xs text-purple-600 mt-1">Ready for compilation</p>
                    )}
                  </div>
                ) : (
                  <>
                    <p className="text-sm font-medium text-gray-900">Click to upload</p>
                    <p className="text-xs text-gray-500 mt-1">
                      {selectedAssignment?.assignmentType === 'CODE'
                        ? '.java or .zip file (max 10MB)'
                        : 'PDF, DOC, DOCX, TXT, ZIP (max 10MB)'}
                    </p>
                  </>
                )}
              </label>
            </div>

            {/* Real-time evaluation status */}
            {evaluationStatus && (
              <div className="mt-3 px-4 py-2 bg-blue-50 border border-blue-200 rounded-lg text-sm text-blue-700">
                {evaluationStatus}
              </div>
            )}
          </div>

          {/* Error Message */}
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-xl text-sm">
              {error}
            </div>
          )}

          {/* Submit Button */}
          <button
            type="submit"
            disabled={uploading || !file}
            className="w-full bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-semibold py-4 rounded-xl shadow-lg hover:shadow-xl transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {uploading ? 'Uploading & Evaluating...' : 'Submit Assignment'}
          </button>
        </form>
      </div>

      {/* Result Display */}
      <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6">
        <h3 className="text-xl font-bold text-gray-900 mb-6">Evaluation Result</h3>

        {result ? (
          <div className="space-y-6">
            {/* Score Display */}
            <div className="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-xl p-6 text-center border border-blue-100">
              <p className="text-sm font-semibold text-gray-600 mb-2">Your Score</p>
              <p className="text-5xl font-bold text-blue-600">{result.score}</p>
              <p className="text-sm text-gray-500 mt-2">out of 100</p>
            </div>

            {/* Assignment Info */}
            <div className="space-y-2">
              <div className="flex justify-between text-sm">
                <span className="text-gray-600">Assignment ID:</span>
                <span className="font-semibold text-gray-900">{result.assignmentId}</span>
              </div>
              {result.submissionId && (
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Submission ID:</span>
                  <span className="font-mono text-xs text-gray-700">{result.submissionId}</span>
                </div>
              )}
            </div>

            {/* Feedback */}
            <div>
              <p className="text-sm font-semibold text-gray-700 mb-2">Feedback:</p>
              <div className="bg-gray-50 rounded-xl p-4 text-sm text-gray-700 leading-relaxed whitespace-pre-wrap">
                {result.feedback}
              </div>
            </div>

            {/* S3 URL */}
            {result.s3Url && (
              <div className="pt-4 border-t border-gray-200">
                <p className="text-xs text-gray-500">File stored successfully in cloud storage</p>
              </div>
            )}
          </div>
        ) : (
          <div className="flex flex-col items-center justify-center h-64 text-gray-400">
            <svg className="w-16 h-16 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            <p className="text-sm">Submit an assignment to see results</p>
          </div>
        )}
      </div>
    </div>
  );
};

const ScoresSection = ({ studentData }) => {
  const [scores, setScores] = useState([]);
  const [loading, setLoading] = useState(true);

  React.useEffect(() => {
    fetchScores();
  }, []);

  const fetchScores = async () => {
    try {
      const response = await fetch(`http://localhost:8080/scores?studentId=${studentData.studentId}`);
      if (response.ok) {
        const data = await response.json();
        setScores(data.scores || []);
      }
    } catch (err) {
      console.error('Failed to fetch scores:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {scores.length > 0 ? (
        scores.map((score, index) => (
          <div
            key={index}
            className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow"
          >
            <div className="flex items-start justify-between">
              <div className="flex-1">
                <h4 className="font-semibold text-gray-900 mb-2">
                  Assignment: {score.assignmentId}
                </h4>
                <p className="text-sm text-gray-600 mb-4 whitespace-pre-wrap">{score.feedback}</p>
                <p className="text-xs text-gray-500">
                  Submitted: {score.submissionTime ? new Date(score.submissionTime).toLocaleString() : 'N/A'}
                </p>
              </div>
              <div className="ml-6 text-center">
                <div className="w-16 h-16 rounded-full bg-blue-50 border-2 border-blue-600 flex items-center justify-center">
                  <span className="text-xl font-bold text-blue-600">{score.score}</span>
                </div>
              </div>
            </div>
          </div>
        ))
      ) : (
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-12 text-center">
          <svg className="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
          <p className="text-gray-500">No scores yet. Submit your first assignment!</p>
        </div>
      )}
    </div>
  );
};

export default StudentDashboard;
