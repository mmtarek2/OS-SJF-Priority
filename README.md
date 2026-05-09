# CPU Scheduling Simulator — SJF vs Priority

A Java Swing app that simulates and compares four scheduling modes:
SJF Non-Preemptive, SJF Preemptive (SRTF), Priority Non-Preemptive, and Priority Preemptive.
Built for the Operating Systems course.


## Team

M1 — SJF algorithm
M2 — Priority algorithm
M3 — Gantt chart
M4 — Documentation
M5 — UI and input
M6 — Results and comparison
M7 — Process model and integration


## How to Run

Step 1 — Make sure Java 8 or newer is installed.
Step 2 — Compile:  javac *.java
Step 3 — Run:  java MainWindow


## How to Use

Step 1 — Add your processes (PID, arrival time, burst time, priority).
Step 2 — Choose a scheduling mode: SJF or Priority, preemptive or non-preemptive.
Step 3 — Click Run Simulation.
Step 4 — View the Gantt charts, results tables, comparison, and conclusion.


## Input Requirements

- PID must be unique and not empty.
- Arrival time must be a whole number ≥ 0.
- Burst time must be a whole number ≥ 1.
- Priority must be a whole number ≥ 1 (lower number = higher priority).


## Metrics

CT — when the process finishes.
TAT — CT minus arrival time.
WT — TAT minus burst time.
RT — first CPU time minus arrival time.


## Scheduling Modes

SJF Non-Preemptive — picks the shortest job when CPU is free, never interrupts.
SJF Preemptive — interrupts the current process if a shorter job arrives.
Priority Non-Preemptive — picks the highest priority job when CPU is free, never interrupts.
Priority Preemptive — interrupts if a higher priority job arrives.


## Project Files

sjf VS priority/ — contains all scheduling algorithm source files.
Documentation/  — contains the project report and notes.
screenshots/    — contains interface and Gantt chart screenshots.

Screenshots are in the screenshots folder.
