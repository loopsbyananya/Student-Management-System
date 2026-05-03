package ui;

import dao.*;
import model.*;
import ui.components.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Extended Admin Dashboard with sidebar navigation, header, and card-based content.
 * Features: Add/View/Remove Students & Teachers, Assign Subjects, Reports, Attendance.
 */
public class AdminDashboard extends JFrame {

    private User currentUser;
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    private SubjectDAO subjectDAO;
    private AttendanceDAO attendanceDAO;
    private AssignmentDAO assignmentDAO;
    private JPanel contentArea;
    private HeaderPanel headerPanel;
    private Sidebar sidebar;

    public AdminDashboard(User user) {
        this.currentUser = user;
        this.studentDAO = new StudentDAO();
        this.teacherDAO = new TeacherDAO();
        this.subjectDAO = new SubjectDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.assignmentDAO = new AssignmentDAO();

        setTitle("Admin Dashboard - " + user.getName());
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 580));
        setLayout(new BorderLayout());

        // --- Sidebar ---
        sidebar = new Sidebar("Admin Panel", user.getName());

        JButton dashBtn = sidebar.addNavButton("Dashboard", "\u2302", e -> showDashboardHome());
        sidebar.addNavButton("Add Student", "+", e -> showAddStudentPanel());
        sidebar.addNavButton("Add Teacher", "+", e -> showAddTeacherPanel());
        sidebar.addNavButton("View Students", "\u2630", e -> showViewStudentsPanel());
        sidebar.addNavButton("View Teachers", "\u2630", e -> showViewTeachersPanel());
        sidebar.addNavButton("Remove Student", "\u2716", e -> showRemoveStudentPanel());
        sidebar.addNavButton("Remove Teacher", "\u2716", e -> showRemoveTeacherPanel());
        sidebar.addNavButton("Update Student", "\u270E", e -> showUpdateStudentPanel());
        sidebar.addNavButton("Update Teacher", "\u270E", e -> showUpdateTeacherPanel());
        sidebar.addNavButton("Assign Subjects", "\u2699", e -> showAssignSubjectPanel());
        sidebar.addNavButton("Search Students", "\u2315", e -> showSearchStudentsPanel());
        sidebar.addNavButton("Attendance", "\u2714", e -> showAllAttendancePanel());
        sidebar.addNavButton("Reports", "\u2261", e -> showReportsPanel());

        sidebar.addSpacer();
        sidebar.addNavButton("Logout", "\u2190", e -> logout());

        add(sidebar, BorderLayout.WEST);

        // --- Right side ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Theme.CONTENT_BG);

        headerPanel = new HeaderPanel("Admin Dashboard", user.getName());
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

        JPanel cardContainer = new JPanel(new GridLayout(1, 3, 25, 25));
        cardContainer.setOpaque(false);
        cardContainer.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        cardContainer.add(createStatCard("Students",
                String.valueOf(studentDAO.getStudentCount()), Theme.PRIMARY, this::showViewStudentsPanel));

        cardContainer.add(createStatCard("Teachers",
                String.valueOf(teacherDAO.getTeacherCount()), Theme.SUCCESS, this::showViewTeachersPanel));

        cardContainer.add(createStatCard("Assignments",
                String.valueOf(assignmentDAO.getAssignmentCount()), Theme.PURPLE, null));

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.add(cardContainer, BorderLayout.CENTER);

        setContent(container);
    }

    private CardPanel createStatCard(String title, String value, Color accent, Runnable action) {
    CardPanel card = new CardPanel();
    if (action != null) card.setClickAction(action);

    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(Theme.FONT_SUBTITLE);
    titleLabel.setForeground(Theme.TEXT_PRIMARY);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel count = new JLabel(value);
    count.setFont(new Font("Segoe UI", Font.BOLD, 42));
    count.setForeground(accent);
    count.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel sub = new JLabel("Total " + title.toLowerCase());
    sub.setFont(Theme.FONT_SMALL);
    sub.setForeground(Theme.TEXT_SECONDARY);
    sub.setAlignmentX(Component.CENTER_ALIGNMENT);

    card.add(titleLabel);
    card.add(Box.createVerticalGlue());
    card.add(count);
    card.add(Box.createVerticalGlue());
    card.add(sub);

    card.setBackground(Theme.CARD_BG);
    card.setOpaque(true);

    return card;
}

    // =========================================================================
    // Add Student
    // =========================================================================
    private void showAddStudentPanel() {
        headerPanel.setTitle("Add Student");
        CardPanel card = new CardPanel("New Student Registration");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField idField = styledField();
        JTextField nameField = styledField();
        JTextField classField = styledField();
        JPasswordField pwField = new JPasswordField();
        styleField(pwField);

        addRow(form, gbc, 0, "Student ID", idField);
        addRow(form, gbc, 1, "Full Name", nameField);
        addRow(form, gbc, 2, "Class", classField);
        addRow(form, gbc, 3, "Password", pwField);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.insets = new Insets(16, 10, 10, 10);
        JPanel bp = btnRow(new StyledButton("Save Student"),
                new StyledButton("Clear", StyledButton.Style.SECONDARY));
        form.add(bp, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        ((StyledButton) bp.getComponent(0)).addActionListener(e -> {
            String id = idField.getText().trim(), name = nameField.getText().trim(),
                   cls = classField.getText().trim(),
                   pw = new String(pwField.getPassword()).trim();
            if (id.isEmpty() || name.isEmpty() || cls.isEmpty() || pw.isEmpty()) {
                warn("All fields are required."); return;
            }
            if (studentDAO.addStudent(new Student(id, name, pw, cls)))
                info("Student added successfully!");
            else err("Failed to add student. ID may already exist.");
        });
        ((StyledButton) bp.getComponent(1)).addActionListener(e -> {
            idField.setText(""); nameField.setText("");
            classField.setText(""); pwField.setText("");
        });
    }

    // =========================================================================
    // Add Teacher
    // =========================================================================
    private void showAddTeacherPanel() {
        headerPanel.setTitle("Add Teacher");
        CardPanel card = new CardPanel("New Teacher Registration");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField idField = styledField();
        JTextField nameField = styledField();
        JTextField subField = styledField();
        JPasswordField pwField = new JPasswordField();
        styleField(pwField);

        addRow(form, gbc, 0, "Teacher ID", idField);
        addRow(form, gbc, 1, "Full Name", nameField);
        addRow(form, gbc, 2, "Subject", subField);
        addRow(form, gbc, 3, "Password", pwField);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton saveBtn = new StyledButton("Save Teacher");
        form.add(saveBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        saveBtn.addActionListener(e -> {
            String id = idField.getText().trim(), name = nameField.getText().trim(),
                   sub = subField.getText().trim(),
                   pw = new String(pwField.getPassword()).trim();
            if (id.isEmpty() || name.isEmpty() || sub.isEmpty() || pw.isEmpty()) {
                warn("All fields are required."); return;
            }
            if (teacherDAO.addTeacher(new Teacher(id, name, pw, sub)))
                info("Teacher added successfully!");
            else err("Failed to add teacher. ID may already exist.");
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
    // View Teachers
    // =========================================================================
    private void showViewTeachersPanel() {
        headerPanel.setTitle("View Teachers");
        CardPanel card = new CardPanel("All Registered Teachers");
        String[] cols = {"Teacher ID", "Name", "Subject"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (Teacher t : teacherDAO.getAllTeachers())
            tm.addRow(new Object[]{t.getId(), t.getName(), t.getSubject()});
        card.add(TableFactory.createStyledTable(tm), BorderLayout.CENTER);
        addRefresh(card, e -> showViewTeachersPanel());
        setContent(card);
    }

    // =========================================================================
    // Remove Student
    // =========================================================================
    private void showRemoveStudentPanel() {
        headerPanel.setTitle("Remove Student");
        CardPanel card = new CardPanel("Remove a Student");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField idField = styledField();
        addRow(form, gbc, 0, "Student ID", idField);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton removeBtn = new StyledButton("Remove Student", StyledButton.Style.DANGER);
        form.add(removeBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        removeBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) { warn("Student ID is required."); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove student " + id + "?\n" +
                    "This will delete all their attendance, marks, and submissions.",
                    "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (studentDAO.deleteStudent(id)) {
                    info("Student removed successfully!");
                    idField.setText("");
                } else err("Failed to remove student. ID may not exist.");
            }
        });
    }

    // =========================================================================
    // Remove Teacher
    // =========================================================================
    private void showRemoveTeacherPanel() {
        headerPanel.setTitle("Remove Teacher");
        CardPanel card = new CardPanel("Remove a Teacher");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField idField = styledField();
        addRow(form, gbc, 0, "Teacher ID", idField);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton removeBtn = new StyledButton("Remove Teacher", StyledButton.Style.DANGER);
        form.add(removeBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        removeBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) { warn("Teacher ID is required."); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove teacher " + id + "?",
                    "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (teacherDAO.deleteTeacher(id)) {
                    info("Teacher removed successfully!");
                    idField.setText("");
                } else err("Failed to remove teacher. ID may not exist.");
            }
        });
    }

    // =========================================================================
    // Update Student
    // =========================================================================
    private void showUpdateStudentPanel() {
        headerPanel.setTitle("Update Student");
        CardPanel card = new CardPanel("Update Student Details");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField idField = styledField();
        JTextField nameField = styledField();
        JTextField classField = styledField();

        StyledButton fetchBtn = new StyledButton("Fetch", StyledButton.Style.SECONDARY);
        JPanel idPanel = new JPanel(new BorderLayout(10, 0));
        idPanel.setOpaque(false);
        idPanel.add(idField, BorderLayout.CENTER);
        idPanel.add(fetchBtn, BorderLayout.EAST);

        addRow(form, gbc, 0, "Student ID", idPanel);
        addRow(form, gbc, 1, "Full Name", nameField);
        addRow(form, gbc, 2, "Class", classField);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton updateBtn = new StyledButton("Update Student");
        form.add(updateBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        fetchBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) { warn("Enter Student ID to fetch."); return; }
            Student s = studentDAO.getStudentById(id);
            if (s != null) {
                nameField.setText(s.getName());
                classField.setText(s.getStudentClass());
                info("Student fetched successfully.");
            } else {
                err("Student not found.");
            }
        });

        updateBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String cls = classField.getText().trim();
            if (id.isEmpty() || name.isEmpty() || cls.isEmpty()) {
                warn("All fields are required."); return;
            }
            Student s = new Student();
            s.setId(id); s.setName(name); s.setStudentClass(cls);
            if (studentDAO.updateStudent(s)) {
                info("Student updated successfully!");
            } else {
                err("Failed to update student.");
            }
        });
    }

    // =========================================================================
    // Update Teacher
    // =========================================================================
    private void showUpdateTeacherPanel() {
        headerPanel.setTitle("Update Teacher");
        CardPanel card = new CardPanel("Update Teacher Details");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField idField = styledField();
        JTextField nameField = styledField();
        JTextField subjectField = styledField();

        StyledButton fetchBtn = new StyledButton("Fetch", StyledButton.Style.SECONDARY);
        JPanel idPanel = new JPanel(new BorderLayout(10, 0));
        idPanel.setOpaque(false);
        idPanel.add(idField, BorderLayout.CENTER);
        idPanel.add(fetchBtn, BorderLayout.EAST);

        addRow(form, gbc, 0, "Teacher ID", idPanel);
        addRow(form, gbc, 1, "Full Name", nameField);
        addRow(form, gbc, 2, "Subject", subjectField);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton updateBtn = new StyledButton("Update Teacher");
        form.add(updateBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        fetchBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) { warn("Enter Teacher ID to fetch."); return; }
            Teacher t = teacherDAO.getTeacherById(id);
            if (t != null) {
                nameField.setText(t.getName());
                subjectField.setText(t.getSubject());
                info("Teacher fetched successfully.");
            } else {
                err("Teacher not found.");
            }
        });

        updateBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String sub = subjectField.getText().trim();
            if (id.isEmpty() || name.isEmpty() || sub.isEmpty()) {
                warn("All fields are required."); return;
            }
            Teacher t = new Teacher();
            t.setId(id); t.setName(name); t.setSubject(sub);
            if (teacherDAO.updateTeacher(t)) {
                info("Teacher updated successfully!");
            } else {
                err("Failed to update teacher.");
            }
        });
    }

    // =========================================================================
    // Assign Students to Subjects
    // =========================================================================
    private void showAssignSubjectPanel() {
        headerPanel.setTitle("Assign Subjects");
        CardPanel card = new CardPanel("Assign Student to a Subject");
        JPanel form = createForm();
        GridBagConstraints gbc = formGBC();

        JTextField studentIdField = styledField();
        // Subject dropdown
        JComboBox<String> subjectCombo = new JComboBox<>();
        subjectCombo.setFont(Theme.FONT_BODY);
        subjectCombo.setPreferredSize(new Dimension(280, 40));
        List<Subject> subjects = subjectDAO.getAllSubjects();
        for (Subject s : subjects)
            subjectCombo.addItem(s.getSubjectId() + " - " + s.getSubjectName());

        addRow(form, gbc, 0, "Student ID", studentIdField);
        addRow(form, gbc, 1, "Subject", subjectCombo);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(16, 10, 10, 10);
        StyledButton assignBtn = new StyledButton("Assign Subject");
        form.add(assignBtn, gbc);
        card.add(form, BorderLayout.CENTER);
        setContent(card);

        assignBtn.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            if (studentId.isEmpty() || subjectCombo.getSelectedItem() == null) {
                warn("Both fields are required."); return;
            }
            String subjectId = ((String) subjectCombo.getSelectedItem()).split(" - ")[0];
            if (subjectDAO.assignStudentToSubject(studentId, subjectId))
                info("Subject assigned successfully!");
            else err("Failed. Student may already be enrolled or ID invalid.");
        });
    }

    // =========================================================================
    // View All Attendance
    // =========================================================================
    private void showAllAttendancePanel() {
        headerPanel.setTitle("All Attendance");
        CardPanel card = new CardPanel("System-wide Attendance Records");
        String[] cols = {"Student ID", "Name", "Date", "Status"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (String[] row : attendanceDAO.getAllAttendance())
            tm.addRow(row);
        card.add(TableFactory.createStyledTable(tm), BorderLayout.CENTER);
        addRefresh(card, e -> showAllAttendancePanel());
        setContent(card);
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

        // Filter combo
        JComboBox<String> filterCombo = new JComboBox<>();
        filterCombo.setFont(Theme.FONT_BODY);
        filterCombo.setPreferredSize(new Dimension(180, 40));
        filterCombo.addItem("All Students");
        // Add class filters
        for (String cls : studentDAO.getAllClasses())
            filterCombo.addItem("Class: " + cls);
        // Add subject filters
        for (Subject sub : subjectDAO.getAllSubjects())
            filterCombo.addItem("Subject: " + sub.getSubjectId() + " - " + sub.getSubjectName());

        searchBar.add(new JLabel("Search:") {{ setFont(Theme.FONT_FIELD_LABEL); setForeground(Theme.TEXT_PRIMARY); }});
        searchBar.add(searchField);
        searchBar.add(searchBtn);
        searchBar.add(Box.createRigidArea(new Dimension(16, 0)));
        searchBar.add(new JLabel("Filter:") {{ setFont(Theme.FONT_FIELD_LABEL); setForeground(Theme.TEXT_PRIMARY); }});
        searchBar.add(filterCombo);

        card.add(searchBar, BorderLayout.NORTH);

        // Results table
        String[] cols = {"Student ID", "Name", "Class"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (Student s : studentDAO.getAllStudents())
            tm.addRow(new Object[]{s.getId(), s.getName(), s.getStudentClass()});
        JScrollPane tableScroll = TableFactory.createStyledTable(tm);
        card.add(tableScroll, BorderLayout.CENTER);
        setContent(card);

        // Search action
        Runnable doSearch = () -> {
            String query = searchField.getText().trim();
            String filter = (String) filterCombo.getSelectedItem();
            List<Student> results;
            if (!query.isEmpty()) {
                results = studentDAO.searchStudents(query);
            } else if (filter != null && filter.startsWith("Class: ")) {
                results = studentDAO.getStudentsByClass(filter.substring(7));
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
    // System Reports
    // =========================================================================
    private void showReportsPanel() {
        headerPanel.setTitle("System Reports");
        JPanel grid = new JPanel(new GridLayout(1, 3, 24, 24));
        grid.setOpaque(false);

        grid.add(createStatCard("Students", String.valueOf(studentDAO.getStudentCount()),
                Theme.PRIMARY, this::showViewStudentsPanel));
        grid.add(createStatCard("Teachers", String.valueOf(teacherDAO.getTeacherCount()),
                Theme.SUCCESS, this::showViewTeachersPanel));
        grid.add(createStatCard("Assignments", String.valueOf(assignmentDAO.getAssignmentCount()),
                Theme.PURPLE, null));

        setContent(grid);
    }

    // =========================================================================
    // Utility helpers
    // =========================================================================
    private void logout() { new LoginFrame().setVisible(true); dispose(); }

    private JPanel createForm() {
        JPanel f = new JPanel(new GridBagLayout());
        f.setOpaque(false);
        return f;
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
        styleField(f);
        return f;
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
        l.setFont(Theme.FONT_FIELD_LABEL);
        l.setForeground(Theme.TEXT_PRIMARY);
        form.add(l, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(field, gbc);
    }

    private JPanel btnRow(JButton... btns) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        for (JButton b : btns) p.add(b);
        return p;
    }

    private void addRefresh(CardPanel card, ActionListener action) {
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bp.setOpaque(false);
        StyledButton rb = new StyledButton("Refresh", StyledButton.Style.SECONDARY);
        rb.addActionListener(action);
        bp.add(rb);
        card.add(bp, BorderLayout.SOUTH);
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.WARNING_MESSAGE);
    }
    private void info(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
