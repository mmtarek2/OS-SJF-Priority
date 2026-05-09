import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {

    Color[] colors = {
        new Color(70, 130, 180),
        new Color(60, 179, 113),
        new Color(210, 105, 30),
        new Color(147, 112, 219),
        new Color(220, 20,  60),
        new Color(32,  178, 170),
        new Color(255, 140,  0),
        new Color(105, 105, 105)
    };

    JPanel         processRowsPanel;
    List<JTextField[]> rows = new ArrayList<>();

    GanttPanel ganttSJF;
    GanttPanel ganttPriority;

    DefaultTableModel sjfModel;
    DefaultTableModel priorityModel;
    JTable sjfTable;
    JTable priorityTable;

    JTextArea queueArea;
    JTextArea comparisonArea;
    JTextArea conclusionArea;

    JLabel statusBar;

    public MainWindow() {
        setTitle("SJF vs Priority Scheduling — CPU Scheduler");
        setSize(1180, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
        setVisible(true);
    }

    void buildUI() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("SJF vs Priority Scheduling — CPU Scheduling Comparison", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 17));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));
        add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildInputPanel(), buildOutputPanel());
        split.setDividerLocation(310);
        add(split, BorderLayout.CENTER);

        statusBar = new JLabel("  Ready. Add processes and click Run.");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setFont(new Font("Arial", Font.PLAIN, 12));
        add(statusBar, BorderLayout.SOUTH);
    }

    JScrollPane buildInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel ruleNote = new JLabel("<html><b>Priority Rule:</b> Lower number = Higher priority<br>(1 = most urgent)</html>");
        ruleNote.setFont(new Font("Arial", Font.PLAIN, 12));
        ruleNote.setAlignmentX(LEFT_ALIGNMENT);
        ruleNote.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        panel.add(ruleNote);
        panel.add(Box.createVerticalStrut(12));

        JLabel procLabel = new JLabel("Processes:");
        procLabel.setFont(new Font("Arial", Font.BOLD, 13));
        procLabel.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(procLabel);
        panel.add(Box.createVerticalStrut(4));

        JPanel colHeaders = new JPanel(new GridLayout(1, 5, 4, 0));
        colHeaders.setMaximumSize(new Dimension(300, 20));
        colHeaders.setAlignmentX(LEFT_ALIGNMENT);
        colHeaders.add(new JLabel("PID",      SwingConstants.CENTER));
        colHeaders.add(new JLabel("Arrival",  SwingConstants.CENTER));
        colHeaders.add(new JLabel("Burst",    SwingConstants.CENTER));
        colHeaders.add(new JLabel("Priority", SwingConstants.CENTER));
        colHeaders.add(new JLabel("",         SwingConstants.CENTER));
        panel.add(colHeaders);
        panel.add(Box.createVerticalStrut(3));

        processRowsPanel = new JPanel();
        processRowsPanel.setLayout(new BoxLayout(processRowsPanel, BoxLayout.Y_AXIS));
        JScrollPane rowsScroll = new JScrollPane(processRowsPanel);
        rowsScroll.setPreferredSize(new Dimension(300, 200));
        rowsScroll.setMaximumSize(new Dimension(300, 200));
        rowsScroll.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(rowsScroll);
        panel.add(Box.createVerticalStrut(8));

        addRow("P1", "0", "8", "3");
        addRow("P2", "1", "4", "1");
        addRow("P3", "2", "9", "2");

        JButton addBtn = new JButton("+ Add Process");
        addBtn.setAlignmentX(LEFT_ALIGNMENT);
        addBtn.addActionListener(e -> addRow("", "", "", ""));
        panel.add(addBtn);
        panel.add(Box.createVerticalStrut(6));

        JButton runBtn = new JButton("Run Simulation");
        runBtn.setFont(new Font("Arial", Font.BOLD, 13));
        runBtn.setBackground(new Color(70, 130, 180));
        runBtn.setForeground(Color.WHITE);
        runBtn.setOpaque(true);
        runBtn.setAlignmentX(LEFT_ALIGNMENT);
        runBtn.setMaximumSize(new Dimension(200, 32));
        runBtn.addActionListener(e -> runSimulation());
        panel.add(runBtn);
        panel.add(Box.createVerticalStrut(6));

        JButton clearBtn = new JButton("Clear");
        clearBtn.setAlignmentX(LEFT_ALIGNMENT);
        clearBtn.addActionListener(e -> clearAll());
        panel.add(clearBtn);
        panel.add(Box.createVerticalStrut(6));

        JButton resetBtn = new JButton("Reset Input");
        resetBtn.setAlignmentX(LEFT_ALIGNMENT);
        resetBtn.setMaximumSize(new Dimension(200, 28));
        resetBtn.setBackground(new Color(180, 80, 80));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setOpaque(true);
        resetBtn.addActionListener(e -> resetInput());
        panel.add(resetBtn);
        panel.add(Box.createVerticalStrut(14));

        JLabel scenLabel = new JLabel("Load Scenario:");
        scenLabel.setFont(new Font("Arial", Font.BOLD, 13));
        scenLabel.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(scenLabel);
        panel.add(Box.createVerticalStrut(5));

        String[] scNames = {
            "Scenario A - Basic Mixed",
            "Scenario B - BT vs Priority Conflict",
            "Scenario C - Starvation Case",
            "Scenario D - Validation Demo"
        };
        Runnable[] scActions = {
            this::loadScenarioA, this::loadScenarioB,
            this::loadScenarioC, this::loadScenarioD
        };

        for (int i = 0; i < 4; i++) {
            final Runnable action = scActions[i];
            JButton sb = new JButton(scNames[i]);
            sb.setAlignmentX(LEFT_ALIGNMENT);
            sb.setMaximumSize(new Dimension(300, 28));
            sb.addActionListener(e -> action.run());
            panel.add(sb);
            panel.add(Box.createVerticalStrut(4));
        }

        return new JScrollPane(panel);
    }

    JScrollPane buildOutputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel ganttTitle = new JLabel("Gantt Charts");
        ganttTitle.setFont(new Font("Arial", Font.BOLD, 14));
        ganttTitle.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(ganttTitle);
        panel.add(Box.createVerticalStrut(5));

        JPanel ganttBoth = new JPanel(new GridLayout(2, 1, 5, 5));
        ganttBoth.setMaximumSize(new Dimension(Integer.MAX_VALUE, 195));
        ganttBoth.setAlignmentX(LEFT_ALIGNMENT);

        ganttSJF = new GanttPanel();
        JPanel sjfBox = new JPanel(new BorderLayout());
        sjfBox.setBorder(BorderFactory.createTitledBorder("SJF (Non-Preemptive)"));
        sjfBox.add(new JScrollPane(ganttSJF), BorderLayout.CENTER);
        ganttBoth.add(sjfBox);

        ganttPriority = new GanttPanel();
        JPanel prBox = new JPanel(new BorderLayout());
        prBox.setBorder(BorderFactory.createTitledBorder("Priority Scheduling (Non-Preemptive, Lower = More Urgent)"));
        prBox.add(new JScrollPane(ganttPriority), BorderLayout.CENTER);
        ganttBoth.add(prBox);

        panel.add(ganttBoth);
        panel.add(Box.createVerticalStrut(10));

        JLabel queueTitle = new JLabel("Ready Queue Log (SJF)");
        queueTitle.setFont(new Font("Arial", Font.BOLD, 14));
        queueTitle.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(queueTitle);
        panel.add(Box.createVerticalStrut(4));

        queueArea = new JTextArea(4, 40);
        queueArea.setEditable(false);
        queueArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        queueArea.setText("Queue log will appear here...");
        JScrollPane queueScroll = new JScrollPane(queueArea);
        queueScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
        queueScroll.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(queueScroll);
        panel.add(Box.createVerticalStrut(10));

        JLabel tablesTitle = new JLabel("Results Tables");
        tablesTitle.setFont(new Font("Arial", Font.BOLD, 14));
        tablesTitle.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(tablesTitle);
        panel.add(Box.createVerticalStrut(5));

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tablesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        tablesPanel.setAlignmentX(LEFT_ALIGNMENT);

        String[] cols = {"PID", "AT", "BT", "PR", "CT", "TAT", "WT", "RT"};

        sjfModel = new DefaultTableModel(cols, 0);
        sjfTable = new JTable(sjfModel);
        sjfTable.setEnabled(false);
        JPanel sjfTableBox = new JPanel(new BorderLayout());
        sjfTableBox.setBorder(BorderFactory.createTitledBorder("SJF Results"));
        sjfTableBox.add(new JScrollPane(sjfTable));
        tablesPanel.add(sjfTableBox);

        priorityModel = new DefaultTableModel(cols, 0);
        priorityTable = new JTable(priorityModel);
        priorityTable.setEnabled(false);
        JPanel prTableBox = new JPanel(new BorderLayout());
        prTableBox.setBorder(BorderFactory.createTitledBorder("Priority Results"));
        prTableBox.add(new JScrollPane(priorityTable));
        tablesPanel.add(prTableBox);

        panel.add(tablesPanel);
        panel.add(Box.createVerticalStrut(10));

        JLabel compTitle = new JLabel("Comparison Summary");
        compTitle.setFont(new Font("Arial", Font.BOLD, 14));
        compTitle.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(compTitle);
        panel.add(Box.createVerticalStrut(4));

        comparisonArea = new JTextArea(5, 40);
        comparisonArea.setEditable(false);
        comparisonArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        comparisonArea.setText("Comparison will appear here...");
        JScrollPane compScroll = new JScrollPane(comparisonArea);
        compScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        compScroll.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(compScroll);
        panel.add(Box.createVerticalStrut(10));

        JLabel conclusionTitle = new JLabel("Final Conclusion");
        conclusionTitle.setFont(new Font("Arial", Font.BOLD, 14));
        conclusionTitle.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(conclusionTitle);
        panel.add(Box.createVerticalStrut(4));

        conclusionArea = new JTextArea(7, 40);
        conclusionArea.setEditable(false);
        conclusionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        conclusionArea.setLineWrap(true);
        conclusionArea.setWrapStyleWord(true);
        conclusionArea.setText("Conclusion will appear here...");
        JScrollPane conclusionScroll = new JScrollPane(conclusionArea);
        conclusionScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        conclusionScroll.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(conclusionScroll);
        panel.add(Box.createVerticalStrut(10));

        return new JScrollPane(panel);
    }

    void addRow(String pid, String at, String bt, String pr) {
        JTextField pidField = new JTextField(pid, 4);
        JTextField atField  = new JTextField(at,  4);
        JTextField btField  = new JTextField(bt,  4);
        JTextField prField  = new JTextField(pr,  4);

        JTextField[] fields = {pidField, atField, btField, prField};

        JPanel row = new JPanel(new GridLayout(1, 5, 4, 0));
        row.setMaximumSize(new Dimension(300, 28));
        row.setAlignmentX(LEFT_ALIGNMENT);
        row.add(pidField); row.add(atField); row.add(btField); row.add(prField);

        JButton removeBtn = new JButton("X");
        removeBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        removeBtn.addActionListener(e -> {
            processRowsPanel.remove(row);
            rows.remove(fields);
            processRowsPanel.revalidate();
            processRowsPanel.repaint();
        });
        row.add(removeBtn);

        rows.add(fields);
        processRowsPanel.add(row);
        processRowsPanel.revalidate();
        processRowsPanel.repaint();
    }

    void resetInput() {
        processRowsPanel.removeAll();
        rows.clear();
        processRowsPanel.revalidate();
        processRowsPanel.repaint();
        addRow("", "", "", "");
        addRow("", "", "", "");
        statusBar.setText("  Input reset. Enter new processes.");
    }

    void loadScenario(String[][] data) {
        processRowsPanel.removeAll();
        rows.clear();
        processRowsPanel.revalidate();
        processRowsPanel.repaint();
        for (String[] d : data) addRow(d[0], d[1], d[2], d[3]);
        statusBar.setText("  Scenario loaded. Click Run Simulation.");
    }

    void loadScenarioA() {
        loadScenario(new String[][]{
            {"P1", "0", "6", "3"},
            {"P2", "1", "4", "1"},
            {"P3", "2", "8", "2"},
            {"P4", "3", "3", "4"},
            {"P5", "4", "5", "2"}
        });
        statusBar.setText("  Scenario A: Basic mixed workload loaded.");
    }

    void loadScenarioB() {
        loadScenario(new String[][]{
            {"P1", "0", "2", "4"},   
            {"P2", "0", "10", "1"}, 
            {"P3", "1", "3", "3"},
            {"P4", "2", "5", "2"}
        });
        statusBar.setText("  Scenario B: Burst vs Priority conflict loaded.");
    }

    void loadScenarioC() {
        loadScenario(new String[][]{
            {"P1", "0", "7", "1"},
            {"P2", "0", "3", "1"},
            {"P3", "0", "5", "1"},
            {"P4", "0", "9", "5"},   
            {"P5", "1", "2", "2"}
        });
        statusBar.setText("  Scenario C: Starvation/fairness case loaded.");
    }

    void loadScenarioD() {
        processRowsPanel.removeAll();
        rows.clear();
        processRowsPanel.revalidate();
        processRowsPanel.repaint();
        addRow("P1", "-1", "5",  "2");
        addRow("P1", "0",  "0",  "1");
        addRow("P2", "1",  "4",  "0"); 
        statusBar.setText("  Scenario D: Invalid data loaded. Click Run to see validation.");
    }


    void runSimulation() {

        if (rows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one process.", "No Processes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Process> processList = new ArrayList<>();
        List<String>  usedPids   = new ArrayList<>();

        for (JTextField[] row : rows) {
            String pid    = row[0].getText().trim();
            String atText = row[1].getText().trim();
            String btText = row[2].getText().trim();
            String prText = row[3].getText().trim();

            if (pid.isEmpty())    { showErr("Process ID cannot be empty.");                      return; }
            if (atText.isEmpty()) { showErr("Arrival Time for " + pid + " cannot be empty.");    return; }
            if (btText.isEmpty()) { showErr("Burst Time for "   + pid + " cannot be empty.");    return; }
            if (prText.isEmpty()) { showErr("Priority for "     + pid + " cannot be empty.");    return; }

            if (usedPids.contains(pid)) { showErr("Duplicate process ID: " + pid); return; }
            usedPids.add(pid);

            int at;
            try { at = Integer.parseInt(atText); } catch (NumberFormatException e) { showErr("Arrival Time for " + pid + " must be an integer."); return; }
            if (at < 0) { showErr("Arrival Time for " + pid + " cannot be negative."); return; }

            int bt;
            try { bt = Integer.parseInt(btText); } catch (NumberFormatException e) { showErr("Burst Time for " + pid + " must be an integer."); return; }
            if (bt < 1) { showErr("Burst Time for " + pid + " must be at least 1."); return; }

            int pr;
            try { pr = Integer.parseInt(prText); } catch (NumberFormatException e) { showErr("Priority for " + pid + " must be an integer."); return; }
            if (pr < 1) { showErr("Priority for " + pid + " must be at least 1 (1 = highest)."); return; }

            processList.add(new Process(pid, at, bt, pr));
        }

        List<String> pidColorList  = new ArrayList<>();
        List<Color>  colorAssigned = new ArrayList<>();
        for (int i = 0; i < processList.size(); i++) {
            pidColorList.add(processList.get(i).pid);
            colorAssigned.add(colors[i % colors.length]);
        }

        ScenarioRunner runner = new ScenarioRunner();
        runner.run(processList);

        ganttSJF.setData(runner.sjfResult.gantt,      pidColorList, colorAssigned);
        ganttPriority.setData(runner.priorityResult.gantt, pidColorList, colorAssigned);

        StringBuilder qLog = new StringBuilder();
        for (String line : runner.sjfResult.queueLog) qLog.append(line).append("\n");
        queueArea.setText(qLog.toString());
        queueArea.setCaretPosition(0);

        sjfModel.setRowCount(0);
        for (Process p : runner.sjfProcs) {
            sjfModel.addRow(new Object[]{
                p.pid, p.arrivalTime, p.burstTime, p.priority,
                p.completionTime, p.turnaroundTime, p.waitingTime, p.responseTime
            });
        }
        sjfModel.addRow(new Object[]{"Avg","-","-","-","-",
            String.format("%.2f", runner.sjfResult.avgTAT),
            String.format("%.2f", runner.sjfResult.avgWT),
            String.format("%.2f", runner.sjfResult.avgRT)});

        priorityModel.setRowCount(0);
        for (Process p : runner.priorityProcs) {
            priorityModel.addRow(new Object[]{
                p.pid, p.arrivalTime, p.burstTime, p.priority,
                p.completionTime, p.turnaroundTime, p.waitingTime, p.responseTime
            });
        }
        priorityModel.addRow(new Object[]{"Avg","-","-","-","-",
            String.format("%.2f", runner.priorityResult.avgTAT),
            String.format("%.2f", runner.priorityResult.avgWT),
            String.format("%.2f", runner.priorityResult.avgRT)});

        double sjWT  = runner.sjfResult.avgWT,      prWT  = runner.priorityResult.avgWT;
        double sjTAT = runner.sjfResult.avgTAT,     prTAT = runner.priorityResult.avgTAT;
        double sjRT  = runner.sjfResult.avgRT,       prRT  = runner.priorityResult.avgRT;

        String wtW  = sjWT  < prWT  ? "SJF"      : (prWT  < sjWT  ? "Priority" : "Tie");
        String tatW = sjTAT < prTAT ? "SJF"      : (prTAT < sjTAT ? "Priority" : "Tie");
        String rtW  = sjRT  < prRT  ? "SJF"      : (prRT  < sjRT  ? "Priority" : "Tie");

        comparisonArea.setText(
            String.format("%-22s %-14s %-14s %s%n", "Metric", "SJF", "Priority", "Winner") +
            "------------------------------------------------------------\n" +
            String.format("%-22s %-14s %-14s %s%n", "Avg Waiting Time",
                String.format("%.2f", sjWT),  String.format("%.2f", prWT),  wtW) +
            String.format("%-22s %-14s %-14s %s%n", "Avg Turnaround Time",
                String.format("%.2f", sjTAT), String.format("%.2f", prTAT), tatW) +
            String.format("%-22s %-14s %-14s %s%n", "Avg Response Time",
                String.format("%.2f", sjRT),  String.format("%.2f", prRT),  rtW) +
            "\nPriority Rule: lower number = higher priority (1 = most urgent).\n" +
            "Tie-break: earlier arrival time, then alphabetical PID."
        );

        StringBuilder conc = new StringBuilder();
        conc.append("=== Analysis Questions ===\n\n");

        conc.append("Q1. Lower avg WT?  -> ").append(wtW)
            .append(String.format("  (SJF=%.2f, Priority=%.2f)\n", sjWT, prWT));

        conc.append("Q2. Lower avg TAT? -> ").append(tatW)
            .append(String.format("  (SJF=%.2f, Priority=%.2f)\n", sjTAT, prTAT));

        conc.append("Q3. SJF favors short jobs? -> Yes. SJF always runs the shortest available burst,\n")
            .append("    so short jobs complete quickly regardless of their priority value.\n");

        conc.append("Q4. Priority favors urgent processes? -> Yes. Priority picks the process with\n")
            .append("    the lowest priority number first, so urgent (low-number) jobs run early\n")
            .append("    even if their burst time is large.\n");

        conc.append("Q5. Starvation risk? -> Priority can starve low-priority (high-number) processes\n")
            .append("    if high-priority jobs keep arriving. SJF can starve long jobs when short\n")
            .append("    jobs keep arriving (convoy avoidance but long-job delay).\n");

        if (sjWT <= prWT) {
            conc.append("Q6. Recommendation: SJF is better for this workload (lower WT/TAT).\n")
                .append("    Use Priority when urgency matters more than efficiency.\n");
        } else {
            conc.append("Q6. Recommendation: Priority Scheduling served urgent processes better here.\n")
                .append("    Use SJF when minimizing average waiting time is the main goal.\n");
        }

        conc.append("\n=== Conclusion ===\n");
        conc.append("- Best avg WT:  ").append(wtW).append("\n");
        conc.append("- Best avg TAT: ").append(tatW).append("\n");
        conc.append("- Best avg RT:  ").append(rtW).append("\n");
        conc.append("- SJF is more efficient (minimizes WT/TAT) but ignores urgency.\n");
        conc.append("- Priority ensures urgent jobs run first but may delay short low-priority jobs.\n");
        conc.append("- Trade-off: efficiency (SJF) vs urgency (Priority).\n");
        conc.append("- Fairer in practice: SJF (based on objective burst time, not assigned values).\n");

        conclusionArea.setText(conc.toString());
        conclusionArea.setCaretPosition(0);

        statusBar.setText("  Done. Processes = " + processList.size());
    }


    void clearAll() {
        processRowsPanel.removeAll();
        rows.clear();
        processRowsPanel.revalidate();
        processRowsPanel.repaint();
        ganttSJF.clear();
        ganttPriority.clear();
        sjfModel.setRowCount(0);
        priorityModel.setRowCount(0);
        queueArea.setText("Queue log will appear here...");
        comparisonArea.setText("Comparison will appear here...");
        conclusionArea.setText("Conclusion will appear here...");
        statusBar.setText("  Cleared.");
        addRow("", "", "", "");
        addRow("", "", "", "");
    }

    private void showErr(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { new MainWindow(); }
        });
    }
}
