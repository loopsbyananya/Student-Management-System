package ui;

import dao.*;
import model.*;
import ui.components.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

/**
 * Extended Teacher Dashboard with sidebar navigation, header, and card-based content.
 * Features: Mark Attendance, Enter Marks, View Students, Add Assignment,
 *           View Student Performance, Send Announcement.
 */
public class TeacherDashboard extends JFrame {

    private User currentUser;
    private AttendanceDAO attendanceDAO;
    private MarksDAO marksDAO;
    private StudentDAO studentDAO;
    private AssignmentDAO assignmentDAO;
    private SubjectDAO subjectDAO;
    private AnnouncementDAO announcementDAO;
    private SubmissionDAO submissionDAO;
    private JPanel contentArea;
    private HeaderPanel headerPanel;
    private Sidebar sidebar;

    public TeacherDashboard(User user) {
        this.currentUser = user;
        this.attendanceDAO = new AttendanceDAO();
        this.marksDAO = new MarksDAO();
        this.studentDAO = new StudentDAO();
        this.assignmentDAO = new AssignmentDAO();
        this.subjectDAO = new SubjectDAO();
        this.announcementDAO = new AnnouncementDAO();
        this.submissionDAO = new SubmissionDAO();

        setTitle("Teacher Dashboard - " + user.getName());
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 580));
        setLayout(new BorderLayout());

        // --- Sidebar ---
        sidebar = new Sidebar("Teacher Panel", user.getName());

        JButton dashBtn = sidebar.addNavButton("Dashboard", "\u2302", e -> showDashboardHome());
        sidebar.addNavButton("Mark Attendance", "\u2714", e -> showMarkAttendancePanel());
        sidebar.addNavButton("Enter Marks", "\u270E", e -> showEnterMarksPanel());
        sidebar.addNavButton("View Students", "\u2630", e -> showViewStudentsPanel());
        sidebar.addNavButton("Add Assignment", "+", e -> showAddAssignmentPanel());
        sidebar.addNavButton("View Submissions", "\u2630", e -> showViewSubmissionsPanel());
        sidebar.addNavButton("Search Students", "\u2315", e -> showSearchStudentsPanel());
        sidebar.addNavButton("Performance", "\u2261", e -> showPerformancePanel());
        sidebar.addNavButton("Announcement", "\u2709", e -> showAnnouncementPanel());

        sidebar.addSpacer();
        sidebar.addNavButton("Logout", "\u2190", e -> logout());

        add(sidebar, BorderLayout.WEST);

        // --- Right side ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Theme.CONTENT_BG);

        headerPanel = new HeaderPanel("Teacher Dashboard", user.getName());
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
    // Dashboard Home
    // =========================================================================
private void showDashboardHome() {
    headerPanel.setTitle("Dashboard");

    JPanel home = new JPanel(new GridLayout(1, 3, 30, 30));
    home.setOpaque(false);
    home.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

    home.add(createActionCard("Mark Attendance", "✔",
            "Record daily attendance", Theme.PRIMARY, e -> showMarkAttendancePanel()));

    home.add(createActionCard("Add Assignment", "+",
            "Create new assignment", Theme.SUCCESS, e -> showAddAssignmentPanel()));

    home.add(createActionCard("Announcement", "✉",
            "Send announcement", Theme.PURPLE, e -> showAnnouncementPanel()));

    JPanel container = new JPanel(new BorderLayout());
    container.setOpaque(false);
    container.add(home, BorderLayout.CENTER);

    setContent(container);
}

    private CardPanel createActionCard(String title, String icon, String desc,
                                        Color accent, ActionListener action) {
        CardPanel card = new CardPanel();
        if (action != null) {
            card.setClickAction(() -> action.actionPerformed(new java.awt.event.ActionEvent(card, java.awt.event.ActionEvent.ACTION_PERFORMED, title)));
        }
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(32, 28, 32, 28)));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        iconLabel.setForeground(accent);
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel tl = new JLabel(title);
        tl.setFont(Theme.FONT_SUBTITLE);
        tl.setForeground(Theme.TEXT_PRIMARY);
        tl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(tl);
        card.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel dl = new JLabel(desc);
        dl.setFont(Theme.FONT_SMALL);
        dl.setForeground(Theme.TEXT_SECONDARY);
        dl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(dl);
        card.add(Box.createRigidArea(new Dimension(0, 16)));

        StyledButton goBtn = new StyledButton("Open");
        goBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        goBtn.addActionListener(action);
        card.add(goBtn);
        return card;
    }

    // =========================================================================
    // Mark Attendance
    // =========================================================================
    private void showMarkAttendancePanel() {
        headerPanel.setTitle("Mark Attendance");
        CardPanel card = new CardPanel("Record Student Attendance");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField studentIdField = styledField();
        JTextField dateField = styledField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent"});
        statusCombo.setFont(Theme.FONT_BODY);
        statusCombo.setPreferredSize(new Dimension(280, 40));

        addRow(form, gbc, 0, "Student ID", studentIdField);
        addRow(form, gbc, 1, "Date (YYYY-MM-DD)", dateField);
        addRow(form, gbc, 2, "Status", statusCombo);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton saveBtn = new StyledButton("Mark Attendance");
        form.add(saveBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        saveBtn.addActionListener(e -> {
            String sid = studentIdField.getText().trim();
            String date = dateField.getText().trim();
            String status = (String) statusCombo.getSelectedItem();
            if (sid.isEmpty() || date.isEmpty()) { warn("Student ID and Date are required."); return; }
            if (attendanceDAO.markAttendance(sid, date, status)) {
                info("Attendance marked successfully!");
                studentIdField.setText(""); dateField.setText("");
            } else err("Failed to mark attendance. Check Student ID.");
        });
    }

    // =========================================================================
    // Enter Marks
    // =========================================================================
    private void showEnterMarksPanel() {
        headerPanel.setTitle("Enter Marks");
        CardPanel card = new CardPanel("Enter Student Marks");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField studentIdField = styledField();
        JTextField subjectField = styledField();
        JTextField marksField = styledField();

        addRow(form, gbc, 0, "Student ID", studentIdField);
        addRow(form, gbc, 1, "Subject", subjectField);
        addRow(form, gbc, 2, "Marks", marksField);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton saveBtn = new StyledButton("Save Marks");
        form.add(saveBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        saveBtn.addActionListener(e -> {
            String sid = studentIdField.getText().trim();
            String sub = subjectField.getText().trim();
            String mt = marksField.getText().trim();
            if (sid.isEmpty() || sub.isEmpty() || mt.isEmpty()) {
                warn("All fields are required."); return;
            }
            int marks;
            try { marks = Integer.parseInt(mt); }
            catch (NumberFormatException ex) { warn("Marks must be a valid number."); return; }
            if (marksDAO.insertMarks(sid, sub, marks)) {
                info("Marks entered successfully!");
                studentIdField.setText(""); subjectField.setText(""); marksField.setText("");
            } else err("Failed to enter marks. Check Student ID.");
        });
    }

    // =========================================================================
    // View Students
    // =========================================================================
    private void showViewStudentsPanel() {
        headerPanel.setTitle("View Students");
        CardPanel card = new CardPanel("All Registered Students");
        String[] cols = {"Student ID", "Name", "Class"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (Student s : studentDAO.getAllStudents())
            tm.addRow(new Object[]{s.getId(), s.getName(), s.getStudentClass()});
        card.add(TableFactory.createStyledTable(tm), BorderLayout.CENTER);
        addRefresh(card, e -> showViewStudentsPanel());
        setContent(card);
    }

    // =========================================================================
    // Add Assignment (NEW)
    // =========================================================================
    private void showAddAssignmentPanel() {
        headerPanel.setTitle("Add Assignment");
        CardPanel card = new CardPanel("Create New Assignment");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField titleField = styledField();
        JTextField descField = styledField();
        JTextField dueDateField = styledField();
        JTextField minFilesField = styledField();
        minFilesField.setText("1"); // Default

        // Subject combo
        JComboBox<String> subjectCombo = new JComboBox<>();
        subjectCombo.setFont(Theme.FONT_BODY);
        subjectCombo.setPreferredSize(new Dimension(280, 40));
        for (Subject s : subjectDAO.getSubjectsByTeacher(currentUser.getId()))
            subjectCombo.addItem(s.getSubjectId() + " - " + s.getSubjectName());

        addRow(form, gbc, 0, "Title", titleField);
        addRow(form, gbc, 1, "Description", descField);
        addRow(form, gbc, 2, "Subject", subjectCombo);
        addRow(form, gbc, 3, "Due Date (YYYY-MM-DD)", dueDateField);
        addRow(form, gbc, 4, "Min Files", minFilesField);

        gbc.gridx = 1; gbc.gridy = 5;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton saveBtn = new StyledButton("Create Assignment");
        form.add(saveBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        saveBtn.addActionListener(e -> {
            String t = titleField.getText().trim();
            String d = descField.getText().trim();
            String due = dueDateField.getText().trim();
            if (t.isEmpty() || due.isEmpty() || subjectCombo.getSelectedItem() == null) {
                warn("Title, Subject, and Due Date are required."); return;
            }
            int minF = 1;
            try { minF = Integer.parseInt(minFilesField.getText().trim()); } catch (Exception ex) { }
            String subId = ((String) subjectCombo.getSelectedItem()).split(" - ")[0];
            Assignment a = new Assignment(t, d, subId, currentUser.getId(), due, minF);
            if (assignmentDAO.addAssignment(a)) {
                info("Assignment created successfully!");
                titleField.setText(""); descField.setText(""); dueDateField.setText(""); minFilesField.setText("1");
            } else err("Failed to create assignment.");
        });
    }

    // =========================================================================
    // View Submissions (NEW)
    // =========================================================================
    private void showViewSubmissionsPanel() {
        headerPanel.setTitle("View Submissions");
        CardPanel card = new CardPanel("Grade Student Submissions");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JComboBox<String> assignmentCombo = new JComboBox<>();
        assignmentCombo.setFont(Theme.FONT_BODY);
        assignmentCombo.setPreferredSize(new Dimension(300, 40));
        for (Assignment a : assignmentDAO.getAllAssignments()) {
            if (a.getTeacherId().equals(currentUser.getId())) {
                assignmentCombo.addItem(a.getAssignmentId() + " - " + a.getTitle());
            }
        }
        
        StyledButton viewBtn = new StyledButton("View Submissions");
        topPanel.add(new JLabel("Select Assignment: "));
        topPanel.add(assignmentCombo);
        topPanel.add(viewBtn);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);
        setContent(card);

        viewBtn.addActionListener(e -> {
            if (assignmentCombo.getSelectedItem() == null) return;
            int aId = Integer.parseInt(((String) assignmentCombo.getSelectedItem()).split(" - ")[0]);
            
            contentPanel.removeAll();
            String[] cols = {"Submission ID", "Student ID", "Student Name", "File Path", "Marks", "Action"};
            DefaultTableModel tm = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            
            List<String[]> subs = submissionDAO.getSubmissionsByAssignmentWithId(aId);
            for (String[] s : subs) {
                tm.addRow(new Object[]{s[0], s[1], s[2], s[3], s[4], "Grade"});
            }
            
            JTable table = new JTable(tm);
            table.setFont(Theme.FONT_TABLE_CELL);
            table.setRowHeight(Theme.TABLE_ROW_HEIGHT);
            table.getTableHeader().setFont(Theme.FONT_TABLE_HEADER);
            
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent ev) {
                    int row = table.rowAtPoint(ev.getPoint());
                    int col = table.columnAtPoint(ev.getPoint());
                    if (row >= 0 && col == 3) {
                        String filePath = (String) tm.getValueAt(row, 3);
                        JOptionPane.showMessageDialog(TeacherDashboard.this, "File path/content:\n" + filePath, "Submission Details", JOptionPane.INFORMATION_MESSAGE);
                    } else if (row >= 0 && col == 5) {
                        String subIdStr = (String) tm.getValueAt(row, 0);
                        String marksStr = JOptionPane.showInputDialog(TeacherDashboard.this, "Enter marks for submission:");
                        if (marksStr != null && !marksStr.trim().isEmpty()) {
                            try {
                                int m = Integer.parseInt(marksStr.trim());
                                if (submissionDAO.updateMarks(Integer.parseInt(subIdStr), m)) {
                                    tm.setValueAt(String.valueOf(m), row, 4);
                                    info("Marks updated!");
                                } else err("Failed to update marks.");
                            } catch (Exception ex) {
                                warn("Invalid marks.");
                            }
                        }
                    }
                }
            });

            contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });
    }

    // =========================================================================
    // View Student Performance — enhanced with insights + report card
    // =========================================================================
    private void showPerformancePanel() {
        headerPanel.setTitle("Student Performance");
        CardPanel card = new CardPanel("View Student Performance");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel label = new JLabel("Student ID:");
        label.setFont(Theme.FONT_FIELD_LABEL);
        label.setForeground(Theme.TEXT_PRIMARY);
        JTextField searchField = styledField();
        searchField.setPreferredSize(new Dimension(200, 40));
        StyledButton searchBtn = new StyledButton("Search");

        searchPanel.add(label);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        JPanel resultsPanel = new JPanel(new BorderLayout(0, 12));
        resultsPanel.setOpaque(false);

        card.add(searchPanel, BorderLayout.NORTH);
        card.add(resultsPanel, BorderLayout.CENTER);
        setContent(card);

        searchBtn.addActionListener(e -> {
            String sid = searchField.getText().trim();
            if (sid.isEmpty()) { warn("Enter a Student ID."); return; }

            resultsPanel.removeAll();

            List<String[]> marks = marksDAO.getMarksByStudent(sid);
            List<String[]> att = attendanceDAO.getAttendanceByStudent(sid);

            // Compute stats
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

            long present = 0;
            for (String[] r : att) if ("Present".equals(r[1])) present++;
            double pct = att.isEmpty() ? 0 : (present * 100.0 / att.size());

            // Stats row
            JPanel statsRow = new JPanel(new GridLayout(1, 3, 12, 0));
            statsRow.setOpaque(false);
            statsRow.setPreferredSize(new Dimension(0, 80));
            statsRow.add(teacherStatCard("Avg Marks", String.format("%.1f", avg), Theme.PRIMARY));
            statsRow.add(teacherStatCard("Attendance", String.format("%.1f%%", pct),
                    pct >= 75 ? Theme.SUCCESS : Theme.DANGER));
            statsRow.add(teacherStatCard("Weak Subject", count > 0 ? weakSubject : "N/A", Theme.AMBER));
            resultsPanel.add(statsRow, BorderLayout.NORTH);

            // Tables
            JPanel grid = new JPanel(new GridLayout(1, 2, 20, 0));
            grid.setOpaque(false);

            CardPanel marksCard = new CardPanel("Marks");
            String[] marksCols = {"Subject", "Marks"};
            DefaultTableModel marksTM = new DefaultTableModel(marksCols, 0);
            for (String[] row : marks) marksTM.addRow(row);
            marksCard.add(TableFactory.createStyledTable(marksTM), BorderLayout.CENTER);
            grid.add(marksCard);

            CardPanel attCard = new CardPanel("Attendance");
            String[] attCols = {"Date", "Status"};
            DefaultTableModel attTM = new DefaultTableModel(attCols, 0);
            for (String[] row : att) attTM.addRow(row);
            attCard.add(TableFactory.createStyledTable(attTM), BorderLayout.CENTER);
            grid.add(attCard);

            resultsPanel.add(grid, BorderLayout.CENTER);

            // Report Card button
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.setOpaque(false);
            StyledButton reportBtn = new StyledButton("Generate Report Card");
            final String studentId = sid;
            reportBtn.addActionListener(ev -> showReportCardForStudent(studentId));
            btnPanel.add(reportBtn);
            resultsPanel.add(btnPanel, BorderLayout.SOUTH);

            resultsPanel.revalidate();
            resultsPanel.repaint();
        });
    }

    private CardPanel teacherStatCard(String title, String value, Color accent) {
        CardPanel c = new CardPanel();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        JLabel vl = new JLabel(value);
        vl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        vl.setForeground(accent);
        vl.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.add(vl);
        JLabel tl = new JLabel(title);
        tl.setFont(Theme.FONT_SMALL);
        tl.setForeground(Theme.TEXT_SECONDARY);
        tl.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.add(tl);
        return c;
    }

    private void showReportCardForStudent(String studentId) {
        List<String[]> marks = marksDAO.getMarksByStudent(studentId);
        List<String[]> att = attendanceDAO.getAttendanceByStudent(studentId);
        double totalGP = 0; int cnt = 0;
        for (String[] r : marks) {
            try {
                int m = Integer.parseInt(r[1]);
                totalGP += (m >= 90 ? 4.0 : m >= 80 ? 3.5 : m >= 70 ? 3.0 : m >= 60 ? 2.5 : m >= 50 ? 2.0 : m >= 40 ? 1.0 : 0.0);
                cnt++;
            } catch (NumberFormatException ignored) { }
        }
        double gpa = cnt == 0 ? 0 : totalGP / cnt;
        long present = 0;
        for (String[] r : att) if ("Present".equals(r[1])) present++;
        double pct = att.isEmpty() ? 0 : (present * 100.0 / att.size());
        String grade = gpa >= 3.5 ? "A" : gpa >= 3.0 ? "B+" : gpa >= 2.5 ? "B" : gpa >= 2.0 ? "C" : gpa >= 1.0 ? "D" : "F";

        StringBuilder sb = new StringBuilder();
        sb.append("======================================\n");
        sb.append("         STUDENT REPORT CARD          \n");
        sb.append("======================================\n\n");
        sb.append("Student ID: ").append(studentId).append("\n\n");
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
        sb.append("--------------------------------------\n");
        sb.append(String.format("GPA:        %.2f / 4.00  (%s)\n", gpa, grade));
        sb.append(String.format("Attendance: %.1f%% (%d/%d)\n", pct, present, att.size()));
        if (pct < 75) sb.append("\n\u26A0 Attendance below 75%\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(440, 380));
        
        JPanel p = new JPanel(new BorderLayout());
        p.add(sp, BorderLayout.CENTER);
        StyledButton exportBtn = new StyledButton("Export to PDF");
        exportBtn.addActionListener(e -> {
            try {
                String filename = "ReportCard_" + studentId + ".pdf";
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filename));
                document.open();
                document.add(new Paragraph(sb.toString()));
                document.close();
                info("PDF saved to project folder: " + filename);
            } catch (Exception ex) {
                err("Failed to save PDF: " + ex.getMessage());
            }
        });
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.add(exportBtn);
        p.add(bp, BorderLayout.SOUTH);
        
        JOptionPane.showMessageDialog(this, p, "Report Card", JOptionPane.PLAIN_MESSAGE);
    }

    // =========================================================================
    // Search & Filter Students
    // =========================================================================
    private void showSearchStudentsPanel() {
        headerPanel.setTitle("Search Students");
        CardPanel card = new CardPanel("Search & Filter");

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchBar.setOpaque(false);
        searchBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JTextField searchField = styledField();
        searchField.setPreferredSize(new Dimension(200, 40));
        StyledButton searchBtn = new StyledButton("Search");

        JComboBox<String> filterCombo = new JComboBox<>();
        filterCombo.setFont(Theme.FONT_BODY);
        filterCombo.setPreferredSize(new Dimension(180, 40));
        filterCombo.addItem("All Students");
        for (Subject sub : subjectDAO.getAllSubjects())
            filterCombo.addItem("Subject: " + sub.getSubjectId() + " - " + sub.getSubjectName());

        searchBar.add(new JLabel("Search:") {{ setFont(Theme.FONT_FIELD_LABEL); setForeground(Theme.TEXT_PRIMARY); }});
        searchBar.add(searchField);
        searchBar.add(searchBtn);
        searchBar.add(Box.createRigidArea(new Dimension(16, 0)));
        searchBar.add(new JLabel("Filter:") {{ setFont(Theme.FONT_FIELD_LABEL); setForeground(Theme.TEXT_PRIMARY); }});
        searchBar.add(filterCombo);

        card.add(searchBar, BorderLayout.NORTH);

        String[] cols = {"Student ID", "Name", "Class"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (Student s : studentDAO.getAllStudents())
            tm.addRow(new Object[]{s.getId(), s.getName(), s.getStudentClass()});
        card.add(TableFactory.createStyledTable(tm), BorderLayout.CENTER);
        setContent(card);

        Runnable doSearch = () -> {
            String query = searchField.getText().trim();
            String filter = (String) filterCombo.getSelectedItem();
            List<Student> results;
            if (!query.isEmpty()) {
                results = studentDAO.searchStudents(query);
            } else if (filter != null && filter.startsWith("Subject: ")) {
                String subId = filter.substring(9).split(" - ")[0];
                results = studentDAO.getStudentsBySubject(subId);
            } else {
                results = studentDAO.getAllStudents();
            }
            tm.setRowCount(0);
            for (Student s : results)
                tm.addRow(new Object[]{s.getId(), s.getName(), s.getStudentClass()});
        };
        searchBtn.addActionListener(e -> doSearch.run());
        filterCombo.addActionListener(e -> { searchField.setText(""); doSearch.run(); });
    }

    // =========================================================================
    // Send Announcement (NEW)
    // =========================================================================
    private void showAnnouncementPanel() {
        headerPanel.setTitle("Send Announcement");
        CardPanel card = new CardPanel("Post an Announcement");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField titleField = styledField();
        
        JComboBox<String> subjectCombo = new JComboBox<>();
        subjectCombo.setFont(Theme.FONT_BODY);
        subjectCombo.setPreferredSize(new Dimension(280, 40));
        subjectCombo.addItem("General (No Subject)");
        for (Subject s : subjectDAO.getSubjectsByTeacher(currentUser.getId())) {
            subjectCombo.addItem(s.getSubjectId() + " - " + s.getSubjectName());
        }

        JTextArea messageArea = new JTextArea(5, 20);
        messageArea.setFont(Theme.FONT_BODY);
        messageArea.setBackground(Theme.FIELD_BG);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        JScrollPane msgScroll = new JScrollPane(messageArea);
        msgScroll.setPreferredSize(new Dimension(280, 120));
        msgScroll.setBorder(null);

        addRow(form, gbc, 0, "Title", titleField);
        addRow(form, gbc, 1, "Subject", subjectCombo);
        addRow(form, gbc, 2, "Message", msgScroll);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton sendBtn = new StyledButton("Send Announcement");
        form.add(sendBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        sendBtn.addActionListener(e -> {
            String t = titleField.getText().trim();
            String msg = messageArea.getText().trim();
            if (t.isEmpty() || msg.isEmpty()) { warn("Title and Message are required."); return; }
            
            String subId = null;
            if (subjectCombo.getSelectedIndex() > 0) {
                subId = ((String) subjectCombo.getSelectedItem()).split(" - ")[0];
            }

            // Use today's date
            String today = java.time.LocalDate.now().toString();
            Announcement a = new Announcement(t, msg, currentUser.getId(), subId, today);
            if (announcementDAO.addAnnouncement(a)) {
                info("Announcement posted successfully!");
                titleField.setText(""); messageArea.setText("");
            } else err("Failed to post announcement.");
        });
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
        styleField(f); return f;
    }
    private void styleField(JComponent f) {
        f.setFont(Theme.FONT_BODY);
        f.setBackground(Theme.FIELD_BG);
        f.setPreferredSize(new Dimension(280, 40));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
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
