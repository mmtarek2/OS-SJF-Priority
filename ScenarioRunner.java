import java.util.*;

public class ScenarioRunner {

    private SJF                 sjf      = new SJF();
    private PriorityScheduling  priority = new PriorityScheduling();

    public List<Process>       sjfProcs;
    public List<Process>       priorityProcs;
    public SJF                 sjfResult;
    public PriorityScheduling  priorityResult;

    public void run(List<Process> processes) {
        sjfProcs      = copy(processes);
        priorityProcs = copy(processes);

        sjf.simulate(sjfProcs);
        priority.simulate(priorityProcs);

        sjfResult      = sjf;
        priorityResult = priority;
    }

    private List<Process> copy(List<Process> src) {
        List<Process> out = new ArrayList<>();
        for (Process p : src)
            out.add(new Process(p.pid, p.arrivalTime, p.burstTime, p.priority));
        return out;
    }
}
