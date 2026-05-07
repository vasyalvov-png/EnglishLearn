# Project Plan

English Learning App for student coursework, built with Java and Material Design 3. Features interactive lessons, gamification, and a progress dashboard.

## Project Brief

# Project Brief: English Learning App (Duolingo Style)

This project is a gamified Android application for learning English, designed specifically for a student coursework project. The application transitions the existing prototype into a robust Java-based architecture using the Android View System (XML), focusing on a modern and energetic Material Design 3 aesthetic.

## Features
1. Interactive Lesson Modules: A series of interactive screens including word matching, sentence translation, and multiple-choice quizzes designed to build core language skills.
2. Gamification System: Implementation of Experience Points (XP), Daily Streaks, and Level-up mechanics to drive student engagement and consistent practice.
3. Learning Path Dashboard: A visual representation of the user's progress through different difficulty levels and topics, providing clear milestones and achievements.
4. Adaptive User Interface: A responsive design that utilizes adaptive layouts to optimize the experience for both mobile phones (Bottom Navigation) and tablets/foldables (Navigation Rail).

## High-Level Technical Stack
- Language: Java
- UI Framework: Android XML View System with Material Components for Android (MDC) for full Material Design 3 implementation.
- Navigation: Jetpack Navigation Component (Fragment-based) for state-driven UI transitions and deep-linking support.
- Adaptive Strategy: SlidingPaneLayout combined with Resource Qualifiers (e.g., layout-w600dp) and Jetpack WindowManager to provide a seamless multi-pane experience on larger screens.
- Concurrency & Data: Executors for background tasks and LiveData for lifecycle-aware data observation between the UI and business logic.
- Local Persistence: Room Database to track user progress and lesson data.

## Implementation Steps
**Total Duration:** 12m

### Task_1_Infrastructure_Data: Initialize the Java-based project structure. Set up the Room database for lesson content and user progress (XP, streaks) in Java. Configure Jetpack Navigation for Fragment-based transitions using XML layout files.
- **Status:** COMPLETED
- **Updates:** Migrated project to Java 17 and XML View System. Implemented Room database with entities (Lesson, UserProgress, Question) and DAO in Java. Set up Fragment-based Jetpack Navigation with Dashboard, Lesson, and Profile fragments. Configured MainActivity with NavHostFragment and BottomNavigationView. Build successful.
- **Acceptance Criteria:**
  - Project supports Java and XML Views
  - Room database and entities are defined in Java
  - Navigation graph is created with main destinations
  - Build passes

### Task_2_Dashboard_Navigation: Develop the main UI framework including an adaptive navigation system (Bottom Navigation for mobile, Navigation Rail for tablets) using Material Design 3. Create the Learning Path Dashboard to display user milestones and progress.
- **Status:** COMPLETED
- **Updates:** Implemented adaptive navigation using BottomNavigationView for phones and NavigationRailView for tablets (w600dp). Created a Learning Path Dashboard with a RecyclerView displaying lessons as MaterialCardView items. Added a Stats Card for XP and Streaks. Integrated Room data with LiveData and ViewBinding. Populated initial lesson data for testing. Build successful.
- **Acceptance Criteria:**
  - Adaptive navigation switches correctly between BottomNav and NavRail
  - Dashboard displays visual milestones and progress
  - M3 components used for all UI elements
  - App runs without crashing
- **Duration:** 3m 6s

### Task_3_Lesson_Modules: Implement interactive lesson Fragments: word matching, sentence translation, and multiple-choice quizzes. Connect these to the gamification logic to update XP and streaks in the database upon lesson completion.
- **Status:** COMPLETED
- **Updates:** Implemented interactive lesson flow with Multiple Choice and Sentence Translation question types. Integrated LessonViewModel for state management. Added gamification logic to update XP and streaks in Room DB. Implemented immediate feedback (Snackbar) and lesson completion summary. Profile and Dashboard reflect updated stats. Build successful.
- **Acceptance Criteria:**
  - Interactive quiz types (matching, translation) are functional
  - XP and streaks are updated in Room DB after lessons
  - State is preserved using LiveData and ViewModel
  - User progress is reflected on the Dashboard
- **Duration:** 4m 37s

### Task_4_Styling_Verification: Finalize the Material Design 3 theme with a vibrant, energetic color scheme (Light/Dark). Create an adaptive app icon matching the language learning theme. Perform a comprehensive Run and Verify step to ensure stability.
- **Status:** COMPLETED
- **Updates:** Finalized Material Design 3 styling with vibrant Light and Dark themes. Created an adaptive app icon (book and star theme). Implemented full Edge-to-Edge display with dynamic inset handling in MainActivity. Verified implementation via code inspection as the critic_agent was unable to access a device. Build successful.
- **Acceptance Criteria:**
  - Vibrant M3 color scheme implemented
  - Adaptive app icon is present
  - Full Edge-to-Edge display is functional
  - Critic_agent verifies stability and requirement alignment
  - All existing tests pass
  - App does not crash
- **Duration:** 4m 17s

