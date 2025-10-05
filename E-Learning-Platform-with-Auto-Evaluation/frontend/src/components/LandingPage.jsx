import React from 'react';

const LandingPage = ({ onGetStarted }) => {
  return (
    <div className="min-h-screen bg-gradient-to-b from-gray-50 to-white relative overflow-hidden">
      {/* Floating gradient orbs */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-72 h-72 bg-blue-400/20 rounded-full mix-blend-multiply filter blur-3xl animate-float"></div>
        <div className="absolute top-1/3 right-1/4 w-72 h-72 bg-purple-400/20 rounded-full mix-blend-multiply filter blur-3xl animate-float-delayed"></div>
        <div className="absolute bottom-1/4 left-1/2 w-72 h-72 bg-indigo-400/20 rounded-full mix-blend-multiply filter blur-3xl animate-float-slow"></div>
      </div>

      {/* Floating tech badges - hidden on mobile */}
      <div className="hidden lg:block absolute inset-0 overflow-hidden pointer-events-none opacity-40">
        {/* Top Left Quadrant */}
        <div className="absolute top-[8%] left-[5%] animate-float-badge-1">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-gray-200/30">
            <span className="text-xs font-semibold text-gray-700">Java</span>
          </div>
        </div>
        <div className="absolute top-[18%] left-[12%] animate-float-badge-2">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-blue-200/30">
            <span className="text-xs font-semibold text-blue-600">React</span>
          </div>
        </div>
        <div className="absolute top-[12%] left-[3%] animate-float-badge-3">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-yellow-200/30">
            <span className="text-xs font-semibold text-yellow-600">JavaScript</span>
          </div>
        </div>
        <div className="absolute top-[25%] left-[8%] animate-float-badge-4">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-red-200/30">
            <span className="text-xs font-semibold text-red-600">Maven</span>
          </div>
        </div>

        {/* Top Right Quadrant */}
        <div className="absolute top-[10%] right-[8%] animate-float-badge-5">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-orange-200/30">
            <span className="text-xs font-semibold text-orange-600">DynamoDB</span>
          </div>
        </div>
        <div className="absolute top-[20%] right-[4%] animate-float-badge-6">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-blue-200/30">
            <span className="text-xs font-semibold text-blue-700">VS Code</span>
          </div>
        </div>
        <div className="absolute top-[15%] right-[14%] animate-float-badge-7">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-purple-200/30">
            <span className="text-xs font-semibold text-purple-600">IntelliJ</span>
          </div>
        </div>
        <div className="absolute top-[6%] right-[12%] animate-float-badge-8">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-indigo-200/30">
            <span className="text-xs font-semibold text-indigo-600">AWS</span>
          </div>
        </div>

        {/* Middle Left */}
        <div className="absolute top-[40%] left-[4%] animate-float-badge-9">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-green-200/30">
            <span className="text-xs font-semibold text-green-600">Node.js</span>
          </div>
        </div>
        <div className="absolute top-[50%] left-[10%] animate-float-badge-10">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-indigo-200/30">
            <span className="text-xs font-semibold text-indigo-600">SQL</span>
          </div>
        </div>
        <div className="absolute top-[35%] left-[2%] animate-float-badge-11">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-gray-200/30">
            <span className="text-xs font-semibold text-gray-700">Git</span>
          </div>
        </div>

        {/* Middle Right */}
        <div className="absolute top-[38%] right-[6%] animate-float-badge-12">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-purple-200/30">
            <span className="text-xs font-semibold text-purple-600">DSA</span>
          </div>
        </div>
        <div className="absolute top-[48%] right-[12%] animate-float-badge-13">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-orange-200/30">
            <span className="text-xs font-semibold text-orange-600">SDLC</span>
          </div>
        </div>

        {/* Bottom Left */}
        <div className="absolute bottom-[20%] left-[6%] animate-float-badge-15">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-orange-200/30">
            <span className="text-xs font-semibold text-orange-700">AWS S3</span>
          </div>
        </div>
        <div className="absolute bottom-[28%] left-[12%] animate-float-badge-16">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-green-200/30">
            <span className="text-xs font-semibold text-green-600">JUnit</span>
          </div>
        </div>

        {/* Bottom Right */}
        <div className="absolute bottom-[22%] right-[8%] animate-float-badge-18">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-blue-200/30">
            <span className="text-xs font-semibold text-blue-700">AWS EC2</span>
          </div>
        </div>
        <div className="absolute bottom-[18%] right-[14%] animate-float-badge-19">
          <div className="px-2.5 py-1.5 bg-white/70 backdrop-blur-sm rounded-lg shadow-sm border border-teal-200/30">
            <span className="text-xs font-semibold text-teal-600">MongoDB</span>
          </div>
        </div>
      </div>

      {/* Navigation */}
      <nav className="relative z-10 px-8 py-6 max-w-7xl mx-auto flex items-center justify-center lg:justify-between">
        <div className="flex items-center gap-3">
          <div className="w-11 h-11 bg-gradient-to-br from-blue-600 via-indigo-600 to-purple-600 rounded-2xl flex items-center justify-center shadow-lg">
            <span className="text-white text-xl font-bold">AS</span>
          </div>
          <span className="text-gray-900 text-2xl font-bold tracking-tight">Atlas School</span>
        </div>
      </nav>

      {/* Hero Section */}
      <div className="relative z-10 max-w-7xl mx-auto px-8 pt-20 pb-32">
        <div className="max-w-4xl mx-auto text-center space-y-12">

          {/* Main Heading */}
          <div className="space-y-6">
            <div className="inline-block">
              <span className="inline-block px-4 py-2 bg-blue-100 text-blue-700 rounded-full text-sm font-semibold mb-6">
                 AI-Powered E-Learning Platform
              </span>
            </div>

            <h1 className="text-5xl sm:text-6xl md:text-7xl lg:text-8xl font-extrabold tracking-tight leading-tight">
              <span className="block text-gray-900">E-Learning with</span>
              <span className="block mt-2 bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-600 bg-clip-text text-transparent">
                Auto Evaluation
              </span>
            </h1>

            <p className="text-lg sm:text-xl md:text-2xl text-gray-600 max-w-3xl mx-auto leading-relaxed font-light">
              Submit assignments and receive instant, intelligent feedback powered by AI.
              Transform the way you learn and evaluate.
            </p>
          </div>

          {/* CTA Button */}
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
            <button
              onClick={onGetStarted}
              className="group relative px-8 py-4 bg-gradient-to-r from-blue-600 to-indigo-600 text-white rounded-xl font-semibold text-lg shadow-lg hover:shadow-xl hover:scale-105 transition-all duration-200 flex items-center gap-2"
            >
              <span>Get Started</span>
              <svg
                className="w-5 h-5 group-hover:translate-x-1 transition-transform"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7l5 5m0 0l-5 5m5-5H6" />
              </svg>
            </button>
          </div>

          {/* Features */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 max-w-2xl mx-auto pt-8">
            <div className="flex items-center gap-3 px-6 py-4 bg-white/80 backdrop-blur-sm rounded-2xl border border-gray-200 shadow-sm">
              <div className="flex-shrink-0 w-10 h-10 bg-blue-100 rounded-xl flex items-center justify-center">
                <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                </svg>
              </div>
              <div className="text-left">
                <p className="font-semibold text-gray-900">AI-Powered Grading</p>
                <p className="text-sm text-gray-600">Instant evaluation</p>
              </div>
            </div>

            <div className="flex items-center gap-3 px-6 py-4 bg-white/80 backdrop-blur-sm rounded-2xl border border-gray-200 shadow-sm">
              <div className="flex-shrink-0 w-10 h-10 bg-indigo-100 rounded-xl flex items-center justify-center">
                <svg className="w-5 h-5 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
              </div>
              <div className="text-left">
                <p className="font-semibold text-gray-900">Real-Time Feedback</p>
                <p className="text-sm text-gray-600">Get results instantly</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Animations */}
      <style jsx="true">{`
        @keyframes float {
          0%, 100% { transform: translate(0, 0) rotate(0deg); }
          33% { transform: translate(30px, -30px) rotate(5deg); }
          66% { transform: translate(-20px, 20px) rotate(-5deg); }
        }
        .animate-float {
          animation: float 8s ease-in-out infinite;
        }
        .animate-float-delayed {
          animation: float 8s ease-in-out 2s infinite;
        }
        .animate-float-slow {
          animation: float 10s ease-in-out 4s infinite;
        }

        @keyframes floatBadge {
          0%, 100% { transform: translateY(0px) translateX(0px); }
          50% { transform: translateY(-15px) translateX(8px); }
        }
        .animate-float-badge-1 { animation: floatBadge 7s ease-in-out infinite; }
        .animate-float-badge-2 { animation: floatBadge 8s ease-in-out 0.5s infinite; }
        .animate-float-badge-3 { animation: floatBadge 9s ease-in-out 1s infinite; }
        .animate-float-badge-4 { animation: floatBadge 7.5s ease-in-out 1.5s infinite; }
        .animate-float-badge-5 { animation: floatBadge 8.5s ease-in-out 2s infinite; }
        .animate-float-badge-6 { animation: floatBadge 9s ease-in-out 0.8s infinite; }
        .animate-float-badge-7 { animation: floatBadge 8s ease-in-out 1.2s infinite; }
        .animate-float-badge-8 { animation: floatBadge 7.8s ease-in-out 1.8s infinite; }
        .animate-float-badge-9 { animation: floatBadge 8.2s ease-in-out 2.5s infinite; }
        .animate-float-badge-10 { animation: floatBadge 7.5s ease-in-out 0.3s infinite; }
        .animate-float-badge-11 { animation: floatBadge 9s ease-in-out 3s infinite; }
        .animate-float-badge-12 { animation: floatBadge 8s ease-in-out 3.5s infinite; }
        .animate-float-badge-13 { animation: floatBadge 7.5s ease-in-out 4s infinite; }
        .animate-float-badge-14 { animation: floatBadge 8.5s ease-in-out 0.2s infinite; }
        .animate-float-badge-15 { animation: floatBadge 9s ease-in-out 2.8s infinite; }
        .animate-float-badge-16 { animation: floatBadge 7.8s ease-in-out 3.2s infinite; }
        .animate-float-badge-17 { animation: floatBadge 8.2s ease-in-out 1.6s infinite; }
        .animate-float-badge-18 { animation: floatBadge 7.6s ease-in-out 2.2s infinite; }
        .animate-float-badge-19 { animation: floatBadge 8.8s ease-in-out 0.6s infinite; }
        .animate-float-badge-20 { animation: floatBadge 7.4s ease-in-out 3.8s infinite; }
      `}</style>
    </div>
  );
};

export default LandingPage;
