package ui;

import dao.*;
import model.*;
import ui.components.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Extended Student Dashboard with sidebar navigation, header, and card-based content.
 * Features: My Attendance, My Marks, View Assignments, Submit Assignment,
 *           Announcements, Performance Tracker, GPA Calculator.
 */
public class StudentDashboard extends JFrame {

    private User currentUser;
    private AttendanceDAO attendanceDAO;
    private MarksDAO marksDAO;
    private AssignmentDAO assignmentDAO;
    private SubmissionDAO submissionDAO;
    private AnnouncementDAO announcementDAO;
    private SubjectDAO subjectDAO;
    private JPanel contentArea;
    private HeaderPanel headerPanel;
    private Sidebar sidebar;

    public StudentDashboard(User user) {
        this.currentUser = user;
        this.attendanceDAO = new AttendanceDAO();
        this.marksDAO = new MarksDAO();
        this.assignmentDAO = new AssignmentDAO();
        this.submissionDAO = new SubmissionDAO();
        this.announcementDAO = new AnnouncementDAO();
        this.subjectDAO = new SubjectDAO();

        setTitle("Student Dashboard - " + user.getName());
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 580));
        setLayout(new BorderLayout());

        // --- Sidebar ---
        sidebar = new Sidebar("Student Panel", user.getName());

        JButton dashBtn = sidebar.addNavButton("Dashboard", "\u2302", e -> showDashboardHome());
        sidebar.addNavButton("My Attendance", "\u2714", e -> showAttendancePanel());
        sidebar.addNavButton("My Marks", "\u270E", e -> showMarksPanel());
        sidebar.addNavButton("My Subjects", "\u25A4", e -> showMySubjectsPanel());
        sidebar.addNavButton("Assignments", "\u2630", e -> showAssignmentsPanel());
        sidebar.addNavButton("Submit Work", "\u2B06", e -> showSubmitAssignmentPanel());
        sidebar.addNavButton("Announcements", "\u2709", e -> showAnnouncementsPanel());
        sidebar.addNavButton("Performance", "\u2261", e -> showPerformancePanel());
        sidebar.addNavButton("GPA", "\u2605", e -> showGPAPanel());

        sidebar.addSpacer();
        sidebar.addNavButton("Logout", "\u2190", e -> logout());

        add(sidebar, BorderLayout.WEST);

        // --- Right side ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Theme.CONTENT_BG);

        headerPanel = new HeaderPanel("Student Dashboard", user.getName());
        rightPanel.add(headerPanel, BorderLayout.NORTH);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(Theme.CONTENT_BG);
        contentArea.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        rightPanel.add(contentArea, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);
        sidebar.setActiveButton(dashBtn);
        showDashboardHome();
    }

    private void setContent(JPanel panel) {
        contentArea.removeAll();
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // =========================================================================
    // Dashboard Home — summary cards
    // =========================================================================
private void showDashboardHome() {
    headerPanel.setTitle("Dashboard");

    JPanel mainContent = new JPanel(new BorderLayout());
    mainContent.setOpaque(false);

    JPanel cardContainer = new JPanel(new GridLayout(1, 3, 25, 25));
    cardContainer.setOpaque(false);
    cardContainer.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

    // Attendance Card
    List<String[]> attendance = attendanceDAO.getAttendanceByStudent(currentUser.getId());
    long present = attendance.stream().filter(a -> "Present".equals(a[1])).count();

    CardPanel attCard = new CardPanel();
    attCard.setLayout(new BorderLayout());
    attCard.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

    JLabel attTitle = new JLabel("Attendance");
    attTitle.setFont(Theme.FONT_SUBTITLE);
    attTitle.setForeground(Theme.TEXT_PRIMARY);
    attTitle.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel attValue = new JLabel(present + "/" + attendance.size());
    attValue.setFont(new Font("Segoe UI", Font.BOLD, 42));
    attValue.setForeground(Theme.PRIMARY);
    attValue.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel attDesc = new JLabel("Days present");
    attDesc.setFont(Theme.FONT_SMALL);
    attDesc.setForeground(Theme.TEXT_SECONDARY);
    attDesc.setHorizontalAlignment(SwingConstants.CENTER);

    attCard.add(attTitle, BorderLayout.NORTH);
    attCard.add(attValue, BorderLayout.CENTER);
    attCard.add(attDesc, BorderLayout.SOUTH);
    cardContainer.add(attCard);

    // Subjects Card
    List<String[]> marks = marksDAO.getMarksByStudent(currentUser.getId());
    
    CardPanel subCard = new CardPanel();
    subCard.setLayout(new BorderLayout());
    subCard.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

    JLabel subTitle = new JLabel("Subjects");
    subTitle.setFont(Theme.FONT_SUBTITLE);
    subTitle.setForeground(Theme.TEXT_PRIMARY);
    subTitle.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel subValue = new JLabel(String.valueOf(marks.size()));
    subValue.setFont(new Font("Segoe UI", Font.BOLD, 42));
    subValue.setForeground(Theme.SUCCESS);
    subValue.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel subDesc = new JLabel("Enrolled subjects");
    subDesc.setFont(Theme.FONT_SMALL);
    subDesc.setForeground(Theme.TEXT_SECONDARY);
    subDesc.setHorizontalAlignment(SwingConstants.CENTER);

    subCard.add(subTitle, BorderLayout.NORTH);
    subCard.add(subValue, BorderLayout.CENTER);
    subCard.add(subDesc, BorderLayout.SOUTH);
    cardContainer.add(subCard);

    // GPA Card
    CardPanel gpaCard = new CardPanel();
    gpaCard.setLayout(new BorderLayout());
    gpaCard.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

    JLabel gpaTitle = new JLabel("GPA");
    gpaTitle.setFont(Theme.FONT_SUBTITLE);
    gpaTitle.setForeground(Theme.TEXT_PRIMARY);
    gpaTitle.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel gpaValue = new JLabel(String.format("%.2f", calculateGPA(marks)));
    gpaValue.setFont(new Font("Segoe UI", Font.BOLD, 42));
    gpaValue.setForeground(Theme.PURPLE);
    gpaValue.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel gpaDesc = new JLabel("Current grade point");
    gpaDesc.setFont(Theme.FONT_SMALL);
    gpaDesc.setForeground(Theme.TEXT_SECONDARY);
    gpaDesc.setHorizontalAlignment(SwingConstants.CENTER);

    gpaCard.add(gpaTitle, BorderLayout.NORTH);
    gpaCard.add(gpaValue, BorderLayout.CENTER);
    gpaCard.add(gpaDesc, BorderLayout.SOUTH);
    cardContainer.add(gpaCard);

    mainContent.add(cardContainer, BorderLayout.CENTER);
    setContent(mainContent);
}

    // =========================================================================
    // My Attendance — with percentage and warning
    // =========================================================================
    private void showAttendancePanel() {
        headerPanel.setTitle("My Attendance");
        CardPanel card = new CardPanel("Attendance Records");
        List<String[]> att = attendanceDAO.getAttendanceByStudent(currentUser.getId());
        String[] cols = {"Date", "Status"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (String[] row : att) tm.addRow(row);
        card.add(TableFactory.createStyledTable(tm), BorderLayout.CENTER);

        // Attendance analysis footer
        long present = 0;
        for (String[] r : att) if ("Present".equals(r[1])) present++;
        double pct = att.isEmpty() ? 0 : (present * 100.0 / att.size());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JLabel pctLabel = new JLabel(String.format("Attendance: %.1f%% (%d/%d days)", pct, present, att.size()));
        pctLabel.setFont(Theme.FONT_BODY_BOLD);
        pctLabel.setForeground(pct >= 75 ? Theme.SUCCESS : Theme.DANGER);
        footer.add(pctLabel);
        if (pct < 75 && !att.isEmpty()) {
            JLabel warnLabel = new JLabel("  \u26A0 Below 75% — Attendance shortage!");
            warnLabel.setFont(Theme.FONT_BODY_BOLD);
            warnLabel.setForeground(Theme.DANGER);
            footer.add(warnLabel);
        }
        StyledButton rb = new StyledButton("Refresh", StyledButton.Style.SECONDARY);
        rb.addActionListener(e -> showAttendancePanel());
        footer.add(rb);
        card.add(footer, BorderLayout.SOUTH);
        setContent(card);
    }

    // =========================================================================
    // My Marks
    // =========================================================================
    private void showMarksPanel() {
        headerPanel.setTitle("My Marks");
        CardPanel card = new CardPanel("Subject-wise Marks");
        String[] cols = {"Subject", "Marks"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (String[] row : marksDAO.getMarksByStudent(currentUser.getId()))
            tm.addRow(row);
        card.add(TableFactory.createStyledTable(tm), BorderLayout.CENTER);
        addRefresh(card, e -> showMarksPanel());
        setContent(card);
    }

    // =========================================================================
    // View Assignments — with Status + Marks columns
    // =========================================================================
    private void showAssignmentsPanel() {
        headerPanel.setTitle("My Assignments");
        CardPanel card = new CardPanel("Assignment Status");
        String[] cols = {"ID", "Title", "Subject", "Due Date", "Status", "Marks", "Desc", "MinFiles"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        List<String[]> statuses = submissionDAO.getAssignmentStatusForStudent(currentUser.getId());
        for (String[] row : statuses) tm.addRow(row);

        JScrollPane scrollPane = TableFactory.createInteractiveTable(tm);
        JTable table = (JTable) scrollPane.getViewport().getView();

        // Color the Status column
        table.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, foc, row, col);
                String status = value != null ? value.toString() : "";
                if ("Submitted".equals(status)) {
                    c.setForeground(Theme.SUCCESS);
                } else {
                    c.setForeground(Theme.DANGER);
                }
                setFont(Theme.FONT_TABLE_CELL);
                setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
                return c;
            }
        });

        // Hide Description and MinFiles columns
        table.getColumnModel().getColumn(6).setMinWidth(0);
        table.getColumnModel().getColumn(6).setMaxWidth(0);
        table.getColumnModel().getColumn(6).setWidth(0);
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setWidth(0);

        // Click row to see full assignment details
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    String id = table.getValueAt(row, 0).toString();
                    String title = table.getValueAt(row, 1).toString();
                    String subject = table.getValueAt(row, 2).toString();
                    String date = table.getValueAt(row, 3).toString();
                    String status = table.getValueAt(row, 4).toString();
                    String marks = table.getValueAt(row, 5).toString();
                    String desc = table.getValueAt(row, 6) != null ? table.getValueAt(row, 6).toString() : "";
                    String minF = table.getValueAt(row, 7).toString();
                    String msg = "Assignment ID: " + id + "\nSubject: " + subject +
                                 "\nDue Date: " + date + "\nStatus: " + status +
                                 "\nMarks: " + marks + "\nMin Files Required: " + minF +
                                 "\n\nDescription:\n" + desc;
                    JOptionPane.showMessageDialog(StudentDashboard.this, msg, "Assignment: " + title, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        card.add(scrollPane, BorderLayout.CENTER);
        addRefresh(card, e -> showAssignmentsPanel());
        setContent(card);
    }

    // =========================================================================
    // Submit Assignment (NEW)
    // =========================================================================
    private void showSubmitAssignmentPanel() {
        headerPanel.setTitle("Submit Assignment");
        CardPanel card = new CardPanel("Submit Your Work");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField assignIdField = styledField();
        JTextField filesField = styledField();
        filesField.setEditable(false);

        StyledButton attachBtn = new StyledButton("Attach Files", StyledButton.Style.SECONDARY);
        JPanel filePanel = new JPanel(new BorderLayout(10, 0));
        filePanel.setOpaque(false);
        filePanel.add(filesField, BorderLayout.CENTER);
        filePanel.add(attachBtn, BorderLayout.EAST);

        attachBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            int ret = chooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                StringBuilder sb = new StringBuilder();
                for (java.io.File f : chooser.getSelectedFiles()) {
                    if (sb.length() > 0) sb.append("; ");
                    sb.append(f.getAbsolutePath());
                }
                filesField.setText(sb.toString());
            }
        });

        addRow(form, gbc, 0, "Assignment ID", assignIdField);
        addRow(form, gbc, 1, "Files", filePanel);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton submitBtn = new StyledButton("Submit");
        form.add(submitBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        submitBtn.addActionListener(e -> {
            String aidStr = assignIdField.getText().trim();
            String filePaths = filesField.getText().trim();
            if (aidStr.isEmpty() || filePaths.isEmpty()) {
                warn("Assignment ID and at least one file are required."); return;
            }
            int aid;
            try { aid = Integer.parseInt(aidStr); }
            catch (NumberFormatException ex) { warn("Assignment ID must be a number."); return; }
            
            // Validate against min_files if possible
            int minF = 1; // default fallback
            for (Assignment a : assignmentDAO.getAssignmentsForStudent(currentUser.getId())) {
                if (a.getAssignmentId() == aid) {
                    minF = a.getMinFiles();
                    break;
                }
            }
            int fileCount = filePaths.split(";").length;
            if (fileCount < minF) {
                err("This assignment requires at least " + minF + " file(s). You attached " + fileCount + ".");
                return;
            }

            Submission s = new Submission(aid, currentUser.getId(), filePaths);
            if (submissionDAO.submitAssignment(s)) {
                info("Assignment submitted successfully!");
                assignIdField.setText(""); filesField.setText("");
            } else err("Failed to submit. You may have already submitted this assignment.");
        });
    }

    // =========================================================================
    // View Announcements (NEW)
    // =========================================================================
    private void showAnnouncementsPanel() {
        headerPanel.setTitle("Announcements");
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);

        List<Announcement> announcements = announcementDAO.getAnnouncementsByStudent(currentUser.getId());

        if (announcements.isEmpty()) {
            CardPanel empty = new CardPanel("No Announcements");
            JLabel msg = new JLabel("There are no announcements at this time.");
            msg.setFont(Theme.FONT_BODY);
            msg.setForeground(Theme.TEXT_SECONDARY);
            msg.setHorizontalAlignment(SwingConstants.CENTER);
            empty.add(msg, BorderLayout.CENTER);
            wrapper.add(empty);
        } else {
            for (Announcement a : announcements) {
                CardPanel aCard = new CardPanel(a.getTitle());
                aCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

                JTextArea msgArea = new JTextArea(a.getMessage());
                msgArea.setFont(Theme.FONT_BODY);
                msgArea.setForeground(Theme.TEXT_PRIMARY);
                msgArea.setBackground(Theme.CARD_BG);
                msgArea.setEditable(false);
                msgArea.setLineWrap(true);
                msgArea.setWrapStyleWord(true);
                msgArea.setBorder(null);
                aCard.add(msgArea, BorderLayout.CENTER);

                String teacherName = a.getTeacherName();
                JLabel dateLabel = new JLabel("Posted: " + a.getDate() +
                        "  |  By: " + (teacherName != null && !teacherName.isEmpty() ? teacherName : a.getTeacherId()));
                dateLabel.setFont(Theme.FONT_SMALL);
                dateLabel.setForeground(Theme.TEXT_SECONDARY);
                aCard.add(dateLabel, BorderLayout.SOUTH);

                wrapper.add(aCard);
                wrapper.add(Box.createRigidArea(new Dimension(0, 16)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setOpaque(false);
        outerPanel.add(scrollPane, BorderLayout.CENTER);

        setContent(outerPanel);
    }

    // =========================================================================
    // My Subjects (NEW)
    // =========================================================================
    private void showMySubjectsPanel() {
        headerPanel.setTitle("My Subjects");
        List<Subject> subjects = subjectDAO.getSubjectsByStudent(currentUser.getId());

        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
        grid.setOpaque(false);

        if (subjects.isEmpty()) {
            JLabel msg = new JLabel("You are not enrolled in any subjects.");
            msg.setFont(Theme.FONT_BODY);
            msg.setForeground(Theme.TEXT_SECONDARY);
            msg.setHorizontalAlignment(SwingConstants.CENTER);
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setOpaque(false);
            emptyPanel.add(msg, BorderLayout.CENTER);
            setContent(emptyPanel);
            return;
        }

        for (Subject s : subjects) {
            CardPanel card = new CardPanel(s.getSubjectId());
            card.setClickAction(() -> showSubjectDetailPanel(s));

            JLabel nameLabel = new JLabel("<html><div style='text-align: center;'>" + s.getSubjectName() + "</div></html>");
            nameLabel.setFont(Theme.FONT_BODY_BOLD);
            nameLabel.setForeground(Theme.TEXT_PRIMARY);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(nameLabel, BorderLayout.CENTER);

            JLabel clickInfo = new JLabel("Click to view details");
            clickInfo.setFont(Theme.FONT_SMALL);
            clickInfo.setForeground(Theme.TEXT_SECONDARY);
            clickInfo.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(clickInfo, BorderLayout.SOUTH);

            grid.add(card);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(grid, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setOpaque(false);
        scrollWrapper.add(scroll, BorderLayout.CENTER);
        setContent(scrollWrapper);
    }

    private void showSubjectDetailPanel(Subject subject) {
        headerPanel.setTitle("Subject: " + subject.getSubjectName());

        JPanel wrapper = new JPanel(new BorderLayout(0, 20));
        wrapper.setOpaque(false);

        // Back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        StyledButton backBtn = new StyledButton("\u2190 Back to Subjects", StyledButton.Style.SECONDARY);
        backBtn.addActionListener(e -> showMySubjectsPanel());
        topPanel.add(backBtn);
        wrapper.add(topPanel, BorderLayout.NORTH);

        JPanel contentGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        contentGrid.setOpaque(false);

        // Assignments Card
        CardPanel assignCard = new CardPanel("Assignments");
        List<Assignment> assignments = assignmentDAO.getAssignmentsBySubject(subject.getSubjectId());
        if (assignments.isEmpty()) {
            JLabel empty = new JLabel("No assignments.");
            empty.setFont(Theme.FONT_BODY);
            empty.setForeground(Theme.TEXT_SECONDARY);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            assignCard.add(empty, BorderLayout.CENTER);
        } else {
            String[] cols = {"Title", "Due Date"};
            DefaultTableModel tm = new DefaultTableModel(cols, 0);
            for (Assignment a : assignments) {
                tm.addRow(new Object[]{a.getTitle(), a.getDueDate()});
            }
            assignCard.add(TableFactory.createStyledTable(tm), BorderLayout.CENTER);
        }
        contentGrid.add(assignCard);

        // Announcements Card
        CardPanel annCard = new CardPanel("Announcements");
        List<Announcement> announcements = announcementDAO.getAnnouncementsBySubject(subject.getSubjectId());
        if (announcements.isEmpty()) {
            JLabel empty = new JLabel("No announcements.");
            empty.setFont(Theme.FONT_BODY);
            empty.setForeground(Theme.TEXT_SECONDARY);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            annCard.add(empty, BorderLayout.CENTER);
        } else {
            JPanel annList = new JPanel();
            annList.setLayout(new BoxLayout(annList, BoxLayout.Y_AXIS));
            annList.setOpaque(false);
            for (Announcement a : announcements) {
                JPanel item = new JPanel(new BorderLayout());
                item.setOpaque(false);
                item.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
                
                JLabel tLabel = new JLabel("<html><b>" + a.getTitle() + "</b> <span style='font-size: 10px; color: gray;'>(" + a.getDate() + ")</span></html>");
                tLabel.setFont(Theme.FONT_BODY);
                item.add(tLabel, BorderLayout.NORTH);
                
                JTextArea mArea = new JTextArea(a.getMessage());
                mArea.setFont(Theme.FONT_SMALL);
                mArea.setLineWrap(true);
                mArea.setWrapStyleWord(true);
                mArea.setEditable(false);
                mArea.setOpaque(false);
                item.add(mArea, BorderLayout.CENTER);
                
                annList.add(item);
            }
            JScrollPane annScroll = new JScrollPane(annList);
            annScroll.setBorder(null);
            annScroll.setOpaque(false);
            annScroll.getViewport().setOpaque(false);
            annCard.add(annScroll, BorderLayout.CENTER);
        }
        contentGrid.add(annCard);

        wrapper.add(contentGrid, BorderLayout.CENTER);
        setContent(wrapper);
    }

    // =========================================================================
    // Performance Tracker — unified view with insights + report card
    // =========================================================================
    private void showPerformancePanel() {
        headerPanel.setTitle("My Performance");
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setOpaque(false);

        // --- Top: stat cards row ---
        List<String[]> marks = marksDAO.getMarksByStudent(currentUser.getId());
        List<String[]> att = attendanceDAO.getAttendanceByStudent(currentUser.getId());

        double avg = 0; int count = 0;
        String weakSubject = "-"; int lowestMarks = Integer.MAX_VALUE;
        for (String[] r : marks) {
            try {
                int m = Integer.parseInt(r[1]);
                avg += m; count++;
                if (m < lowestMarks) { lowestMarks = m; weakSubject = r[0]; }
            } catch (NumberFormatException ignored) { }
        }
        if (count > 0) avg /= count;
        double gpa = calculateGPA(marks);

        long present = 0;
        for (String[] r : att) if ("Present".equals(r[1])) present++;
        double pct = att.isEmpty() ? 0 : (present * 100.0 / att.size());

        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        statsRow.setPreferredSize(new Dimension(0, 130));

        statsRow.add(miniStatCard("Average Marks", String.format("%.1f", avg), Theme.PRIMARY));
        statsRow.add(miniStatCard("GPA", String.format("%.2f", gpa), gpa >= 3.0 ? Theme.SUCCESS : Theme.AMBER));
        statsRow.add(miniStatCard("Attendance", String.format("%.1f%%", pct), pct >= 75 ? Theme.SUCCESS : Theme.DANGER));
        statsRow.add(miniStatCard("Weak Subject", count > 0 ? weakSubject : "N/A", Theme.DANGER));
        wrapper.add(statsRow, BorderLayout.NORTH);

        // --- Attendance warning ---
        if (pct < 75 && !att.isEmpty()) {
            JPanel warnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            warnPanel.setOpaque(false);
            JLabel warnLabel = new JLabel("\u26A0 Your attendance is below 75%. You may face detention.");
            warnLabel.setFont(Theme.FONT_BODY_BOLD);
            warnLabel.setForeground(Theme.DANGER);
            warnPanel.add(warnLabel);
        }

        // --- Bottom: tables ---
        JPanel grid = new JPanel(new GridLayout(1, 2, 20, 0));
        grid.setOpaque(false);

        CardPanel marksCard = new CardPanel("Marks Summary");
        String[] mCols = {"Subject", "Marks", "Grade"};
        DefaultTableModel mTM = new DefaultTableModel(mCols, 0);
        for (String[] r : marks) {
            try {
                int m = Integer.parseInt(r[1]);
                String grade = m >= 90 ? "A" : m >= 80 ? "B+" : m >= 70 ? "B" : m >= 60 ? "C" : m >= 50 ? "D" : "F";
                mTM.addRow(new Object[]{r[0], r[1], grade});
            } catch (NumberFormatException e2) {
                mTM.addRow(new Object[]{r[0], r[1], "N/A"});
            }
        }
        marksCard.add(TableFactory.createStyledTable(mTM), BorderLayout.CENTER);
        grid.add(marksCard);

        CardPanel attCard = new CardPanel("Attendance Summary");
        String[] aCols = {"Date", "Status"};
        DefaultTableModel aTM = new DefaultTableModel(aCols, 0);
        for (String[] r : att) aTM.addRow(r);
        attCard.add(TableFactory.createStyledTable(aTM), BorderLayout.CENTER);
        grid.add(attCard);

        wrapper.add(grid, BorderLayout.CENTER);

        // --- Report Card button ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        StyledButton reportBtn = new StyledButton("Generate Report Card");
        reportBtn.addActionListener(e -> showReportCardDialog());
        btnPanel.add(reportBtn);
        wrapper.add(btnPanel, BorderLayout.SOUTH);

        setContent(wrapper);
    }

    /** Small stat card for the performance dashboard row. */
    private CardPanel miniStatCard(String title, String value, Color accent) {
        CardPanel card = new CardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel vl = new JLabel(value);
        vl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        vl.setForeground(accent);
        vl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(vl);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        JLabel tl = new JLabel(title);
        tl.setFont(Theme.FONT_SMALL);
        tl.setForeground(Theme.TEXT_SECONDARY);
        tl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(tl);
        return card;
    }

    // =========================================================================
    // Report Card Dialog
    // =========================================================================
    private void showReportCardDialog() {
        List<String[]> marks = marksDAO.getMarksByStudent(currentUser.getId());
        List<String[]> att = attendanceDAO.getAttendanceByStudent(currentUser.getId());
        double gpa = calculateGPA(marks);
        long present = 0;
        for (String[] r : att) if ("Present".equals(r[1])) present++;
        double pct = att.isEmpty() ? 0 : (present * 100.0 / att.size());
        String grade = gpa >= 3.5 ? "A (Excellent)" : gpa >= 3.0 ? "B+ (Very Good)" :
                       gpa >= 2.5 ? "B (Good)" : gpa >= 2.0 ? "C (Average)" :
                       gpa >= 1.0 ? "D (Below Average)" : "F (Fail)";

        StringBuilder sb = new StringBuilder();
        sb.append("======================================\n");
        sb.append("         STUDENT REPORT CARD          \n");
        sb.append("======================================\n\n");
        sb.append("Name:       ").append(currentUser.getName()).append("\n");
        sb.append("Student ID: ").append(currentUser.getId()).append("\n\n");
        sb.append("--------------------------------------\n");
        sb.append(String.format("%-20s %6s %6s\n", "Subject", "Marks", "Grade"));
        sb.append("--------------------------------------\n");
        for (String[] r : marks) {
            try {
                int m = Integer.parseInt(r[1]);
                String g = m >= 90 ? "A" : m >= 80 ? "B+" : m >= 70 ? "B" : m >= 60 ? "C" : m >= 50 ? "D" : "F";
                sb.append(String.format("%-20s %6s %6s\n", r[0], r[1], g));
            } catch (NumberFormatException e2) {
                sb.append(String.format("%-20s %6s %6s\n", r[0], r[1], "N/A"));
            }
        }
        sb.append("--------------------------------------\n\n");
        sb.append(String.format("GPA:        %.2f / 4.00\n", gpa));
        sb.append("Grade:      ").append(grade).append("\n");
        sb.append(String.format("Attendance: %.1f%% (%d/%d days)\n", pct, present, att.size()));
        if (pct < 75) sb.append("\n\u26A0 WARNING: Attendance below 75%\n");
        sb.append("\n======================================\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        JScrollPane sp = new JScrollPane(textArea);
        sp.setPreferredSize(new Dimension(450, 420));
        JOptionPane.showMessageDialog(this, sp, "Report Card — " + currentUser.getName(), JOptionPane.PLAIN_MESSAGE);
    }

    // =========================================================================
    // GPA Calculator (NEW)
    // =========================================================================
    private void showGPAPanel() {
        headerPanel.setTitle("GPA Calculator");
        CardPanel card = new CardPanel("Your GPA");

        List<String[]> marks = marksDAO.getMarksByStudent(currentUser.getId());
        double gpa = calculateGPA(marks);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel gpaLabel = new JLabel(String.format("%.2f", gpa));
        gpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 72));
        gpaLabel.setForeground(gpa >= 3.5 ? Theme.SUCCESS :
                               gpa >= 2.5 ? Theme.PRIMARY :
                               gpa >= 1.5 ? Theme.AMBER : Theme.DANGER);
        gpaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(gpaLabel);

        JLabel scale = new JLabel("out of 4.00");
        scale.setFont(Theme.FONT_SUBTITLE);
        scale.setForeground(Theme.TEXT_SECONDARY);
        scale.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(scale);

        center.add(Box.createRigidArea(new Dimension(0, 20)));

        // Grade breakdown
        String grade = gpa >= 3.5 ? "A (Excellent)" :
                       gpa >= 3.0 ? "B+ (Very Good)" :
                       gpa >= 2.5 ? "B (Good)" :
                       gpa >= 2.0 ? "C (Average)" :
                       gpa >= 1.0 ? "D (Below Average)" : "F (Fail)";
        JLabel gradeLabel = new JLabel("Grade: " + grade);
        gradeLabel.setFont(Theme.FONT_SUBTITLE);
        gradeLabel.setForeground(Theme.TEXT_PRIMARY);
        gradeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(gradeLabel);

        center.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel subCount = new JLabel("Based on " + marks.size() + " subject(s)");
        subCount.setFont(Theme.FONT_SMALL);
        subCount.setForeground(Theme.TEXT_SECONDARY);
        subCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(subCount);

        center.add(Box.createRigidArea(new Dimension(0, 20)));

        // Subject-wise table
        String[] cols = {"Subject", "Marks", "Grade Point"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (String[] row : marks) {
            try {
                int m = Integer.parseInt(row[1]);
                double gp = marksToGradePoint(m);
                tm.addRow(new Object[]{row[0], row[1], String.format("%.1f", gp)});
            } catch (NumberFormatException ignored) {
                tm.addRow(new Object[]{row[0], row[1], "N/A"});
            }
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(center, BorderLayout.NORTH);
        wrapper.add(TableFactory.createStyledTable(tm), BorderLayout.CENTER);

        card.add(wrapper, BorderLayout.CENTER);
        setContent(card);
    }

    /**
     * Calculates GPA based on marks average (simple 4.0 scale).
     * Formula: GPA = average of grade points per subject.
     */
    private double calculateGPA(List<String[]> marks) {
        if (marks.isEmpty()) return 0.0;
        double totalGP = 0;
        int count = 0;
        for (String[] row : marks) {
            try {
                int m = Integer.parseInt(row[1]);
                totalGP += marksToGradePoint(m);
                count++;
            } catch (NumberFormatException ignored) { }
        }
        return count == 0 ? 0.0 : totalGP / count;
    }

    /** Converts marks (0-100) to a grade point (0-4.0 scale). */
    private double marksToGradePoint(int marks) {
        if (marks >= 90) return 4.0;
        if (marks >= 80) return 3.5;
        if (marks >= 70) return 3.0;
        if (marks >= 60) return 2.5;
        if (marks >= 50) return 2.0;
        if (marks >= 40) return 1.0;
        return 0.0;
    }

    // =========================================================================
    // Utility helpers
    // =========================================================================
    private void logout() { new LoginFrame().setVisible(true); dispose(); }

    private JPanel createForm() {
        JPanel f = new JPanel(new GridBagLayout()); f.setOpaque(false); return f;
    }
    private GridBagConstraints formGBC() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        return g;
    }
    private JTextField styledField() {
        JTextField f = new JTextField(20);
        f.setFont(Theme.FONT_BODY);
        f.setBackground(Theme.FIELD_BG);
        f.setPreferredSize(new Dimension(280, 40));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        return f;
    }
    private void addRow(JPanel form, GridBagConstraints gbc,
                        int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel l = new JLabel(label);
        l.setFont(Theme.FONT_FIELD_LABEL); l.setForeground(Theme.TEXT_PRIMARY);
        form.add(l, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(field, gbc);
    }
    private void addRefresh(CardPanel card, ActionListener action) {
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bp.setOpaque(false);
        StyledButton rb = new StyledButton("Refresh", StyledButton.Style.SECONDARY);
        rb.addActionListener(action); bp.add(rb);
        card.add(bp, BorderLayout.SOUTH);
    }
    private void warn(String m) { JOptionPane.showMessageDialog(this, m, "Input Error", JOptionPane.WARNING_MESSAGE); }
    private void info(String m) { JOptionPane.showMessageDialog(this, m, "Success", JOptionPane.INFORMATION_MESSAGE); }
    private void err(String m) { JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE); }
}
