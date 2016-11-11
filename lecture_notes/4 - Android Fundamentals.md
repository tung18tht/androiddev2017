# Android Fundamentals

### Group 5
* Nguyen Duc Tung - USTHBI5 - 146
* Dang Vu Lam - USTHBI4 - 078
* Do Dang Ngoc Kha - USTHBI5 - 061

## I. Chapter Objectives

* Introduction to the architecture of Android, the MVC model and compilation in Android programming.

## II. Architecture

* Four main layers:

  * Applications: written in Java, main focus for the course. Example: weather, contacts, phone, browsers, etc.
  * Application Framework: higher level, also in Java, UI, Location Service, Notification, etc.
  * Libraries: mainly in C, C++, low level.
  * Linux Kernel: well-shaped, secured, active development.

## III. Compilation

* Java compiler: compile once, run on multiple platforms.
* Android Virtual Machines
  * Dalvik: the runtime, bytecode, and VM used by the Android system for running Android applications.
  * ART (Android RunTime): next version of Dalvik, faster, improved Garbage Collection (GC), first introduced in Android KitKat.

## IV. MVC Model

* Model: manages behavior and data.
* View: manages the display of information.
* Controller: controls the data flow into model object and updates the view whenever data changes.

## V. Controllers

* Context
  * Central command center.
  * System services.
  * Access application-specific data.

* Application
  * A context.
  * Can be subclassed.
  * Android memory management.
  * AndroidManifest.xml.

* Activity
  * Fundamental building block.
  * Has a unique task or purpose.
  * Every application has at least one activity.
  * Handles display of single screen.

* Activity lifecycle
  * `onCreate()`: initialization.
  * `onStart()`: visible state.
  * `onPause()`: Stop animation or heavy tasks, save unsaved changes.
  * `onResume()`: Called when activity comes to foreground.
  * Screen orientation
    * `onSaveInstanceState()`
    * `onDestroy()`
  * New activity instance
    * `onCreate()`
    * `onRestoreInstanceState()`
  * Close current activity: `finish()`
  * Intent: used to move from one activity to another.

* Fragment
  * A behavior or a portion of the UI.
  * Building block of the Fundamental building blocks.
  * Optional.
  * Officially supported from Honeycomb [API 11].
  * Similar lifecycle as Activity.

## VI. View

* Interact with user
* Building blocks for User Interface
* XML or dynamic code
* Viewgroup: Contain children; Layouts
