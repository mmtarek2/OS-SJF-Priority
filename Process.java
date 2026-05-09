public class Process {

    String pid;
    int    arrivalTime;
    int    burstTime;
    int    priority;

    int completionTime;
    int waitingTime;
    int turnaroundTime;
    int responseTime;

    int     remainingTime;
    boolean firstRun;

    public Process(String pid, int arrivalTime, int burstTime, int priority) {
        this.pid         = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime   = burstTime;
        this.priority    = priority;
        reset();
    }

    public void reset() {
        this.remainingTime  = burstTime;
        this.completionTime = 0;
        this.waitingTime    = 0;
        this.turnaroundTime = 0;
        this.responseTime   = -1;
        this.firstRun       = true;
    }

    @Override
    public String toString() {
        return String.format("%-5s  AT=%-3d  BT=%-3d  PR=%-3d", pid, arrivalTime, burstTime, priority);
    }
}
