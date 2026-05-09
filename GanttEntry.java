public class GanttEntry {

    String pid;  
    int    start; 
    int    end;   

    public GanttEntry(String pid, int start, int end) {
        this.pid   = pid;
        this.start = start;
        this.end   = end;
    }

    @Override
    public String toString() {
        return String.format("[%s  %d→%d]", pid, start, end);
    }
}
