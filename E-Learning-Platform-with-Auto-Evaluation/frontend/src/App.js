import React, { useState } from 'react';
import LandingPage from './components/LandingPage';
import AuthModal from './components/AuthModal';
import StudentDashboard from './components/StudentDashboard';
import TeacherDashboard from './components/TeacherDashboard';

function App() {
  const [showAuthModal, setShowAuthModal] = useState(false);
  const [showRoleSelection, setShowRoleSelection] = useState(false);
  const [selectedRole, setSelectedRole] = useState(null);
  const [studentData, setStudentData] = useState(null);
  const [isTeacher, setIsTeacher] = useState(false);

  const handleGetStarted = () => {
    setShowRoleSelection(true);
  };

  const handleRoleSelect = (role) => {
    setSelectedRole(role);
    setShowRoleSelection(false);
    if (role === 'teacher') {
      setIsTeacher(true);
    } else {
      setShowAuthModal(true);
    }
  };

  const handleLogin = (data) => {
    setStudentData(data);
    setShowAuthModal(false);
  };

  const handleCloseAuth = () => {
    setShowAuthModal(false);
    setSelectedRole(null);
  };

  const handleCloseRoleSelection = () => {
    setShowRoleSelection(false);
  };

  const handleLogout = () => {
    setStudentData(null);
    setIsTeacher(false);
    setSelectedRole(null);
  };

  return (
    <div className="App">
      {!studentData && !isTeacher ? (
        <>
          <LandingPage onGetStarted={handleGetStarted} />
          <RoleSelectionModal
            isOpen={showRoleSelection}
            onClose={handleCloseRoleSelection}
            onSelect={handleRoleSelect}
          />
          <AuthModal
            isOpen={showAuthModal}
            onClose={handleCloseAuth}
            onLogin={handleLogin}
          />
        </>
      ) : isTeacher ? (
        <TeacherDashboard onLogout={handleLogout} />
      ) : (
        <StudentDashboard studentData={studentData} onLogout={handleLogout} />
      )}
    </div>
  );
}

const RoleSelectionModal = ({ isOpen, onClose, onSelect }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black bg-opacity-50 backdrop-blur-sm animate-fadeIn">
      <div className="relative bg-white rounded-2xl shadow-2xl max-w-md w-full p-8 transform transition-all animate-slideUp">
        {/* Close Button */}
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-gray-400 hover:text-gray-600 transition-colors"
        >
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>

        {/* Modal Content */}
        <div className="text-center space-y-6">
          <div>
            <h2 className="font-heading text-3xl font-bold text-gray-800 mb-2">
              Choose Your Role
            </h2>
            <p className="text-gray-600">
              Are you a teacher or a student?
            </p>
          </div>

          <div className="space-y-4 pt-4">
            {/* Teacher Button */}
            <button
              onClick={() => onSelect('teacher')}
              className="w-full group relative overflow-hidden bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-700 hover:to-pink-700 text-white font-semibold py-4 px-6 rounded-xl shadow-lg hover:shadow-xl transform hover:scale-105 transition-all duration-200"
            >
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                  </svg>
                  <span>I'm a Teacher</span>
                </div>
                <svg className="w-5 h-5 transform group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </button>

            {/* Student Button */}
            <button
              onClick={() => onSelect('student')}
              className="w-full group relative overflow-hidden bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-semibold py-4 px-6 rounded-xl shadow-lg hover:shadow-xl transform hover:scale-105 transition-all duration-200"
            >
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                  <span>I'm a Student</span>
                </div>
                <svg className="w-5 h-5 transform group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </button>
          </div>
        </div>
      </div>

      {/* Animations */}
      <style jsx="true">{`
        @keyframes fadeIn {
          from { opacity: 0; }
          to { opacity: 1; }
        }
        @keyframes slideUp {
          from {
            opacity: 0;
            transform: translateY(20px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
        .animate-fadeIn {
          animation: fadeIn 0.2s ease-out;
        }
        .animate-slideUp {
          animation: slideUp 0.3s ease-out;
        }
      `}</style>
    </div>
  );
};

export default App;
