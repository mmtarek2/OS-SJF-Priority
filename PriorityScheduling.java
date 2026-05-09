import java.util.*;

public class PriorityScheduling {

    List<GanttEntry> gantt    = new ArrayList<>();
    List<String>     queueLog = new ArrayList<>();
    double avgWT  = 0;
    double avgTAT = 0;
    double avgRT  = 0;

    public void simulate(List<Process> original) {

        List<Process> procs = deepCopy(original);
        procs.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int n    = procs.size();
        int done = 0;
        int time = 0;

        gantt.clear();
        queueLog.clear();

        while (done < n) {

            List<Process> ready = new ArrayList<>();
            for (Process p : procs) {
                if (p.arrivalTime <= time && p.remainingTime > 0)
                    ready.add(p);
            }

            if (ready.isEmpty()) {
                int nextAt = Integer.MAX_VALUE;
                for (Process p : procs) {
                    if (p.remainingTime > 0 && p.arrivalTime > time)
                        nextAt = Math.min(nextAt, p.arrivalTime);
                }
                gantt.add(new GanttEntry("IDLE", time, nextAt));
                time = nextAt;
                continue;
            }

            ready.sort(Comparator.comparingInt((Process p) -> p.priority)
                                 .thenComparingInt(p -> p.arrivalTime)
                                 .thenComparing(p -> p.pid));

            StringBuilder snap = new StringBuilder("t=" + time + " | Ready: ");
            for (int i = 0; i < ready.size(); i++) {
                if (i > 0) snap.append(", ");
                snap.append(ready.get(i).pid).append("(PR=").append(ready.get(i).priority).append(")");
            }
            queueLog.add(snap.toString());

            Process current = ready.get(0);

            if (current.firstRun) {
                current.responseTime = time - current.arrivalTime;
                current.firstRun = false;
            }

            int start = time;
            time += current.burstTime;
            current.remainingTime   = 0;
            current.completionTime  = time;
            current.turnaroundTime  = time - current.arrivalTime;
            current.waitingTime     = current.turnaroundTime - current.burstTime;

            gantt.add(new GanttEntry(current.pid, start, time));
            done++;
        }

        double sumWT = 0, sumTAT = 0, sumRT = 0;
        for (Process p : procs) { sumWT += p.waitingTime; sumTAT += p.turnaroundTime; sumRT += p.responseTime; }
        avgWT  = sumWT  / n;
        avgTAT = sumTAT / n;
        avgRT  = sumRT  / n;

        for (Process orig : original) {
            for (Process sim : procs) {
                if (orig.pid.equals(sim.pid)) {
                    orig.completionTime = sim.completionTime;
                    orig.waitingTime    = sim.waitingTime;
                    orig.turnaroundTime = sim.turnaroundTime;
                    orig.responseTime   = sim.responseTime;
                    break;
                }
            }
        }
    }

    private List<Process> deepCopy(List<Process> src) {
        List<Process> copy = new ArrayList<>();
        for (Process p : src)
            copy.add(new Process(p.pid, p.arrivalTime, p.burstTime, p.priority));
        return copy;
    }
}
