import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class ReclamationDetailsWindow extends JFrame {
    private JPanel contentPane;
    private int ID;
    private String nom;
    private String type;
    private String localisation;
    private Date date_creation;
    private Date date_resolution;
    private String description;
    private String status;
    private String CIN;

    private JTextArea detailsArea;

    public ReclamationDetailsWindow(int ID, String nom, String type, String localisation, Date date_creation, Date date_resolution, String description, String status, String CIN) {
        this.ID = ID;
        this.nom = nom;
        this.type = type;
        this.localisation = localisation;
        this.date_creation = date_creation;
        this.date_resolution = date_resolution;
        this.description = description;
        this.status = status;
        this.CIN = CIN;

        setTitle(" Mon Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 846, 482);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(Login.class.getResource("/image/back.PNG")));
        lblNewLabel.setBounds(0, 0, 866, 82);
        contentPane.add(lblNewLabel);

        JLabel lblDetails = new JLabel("Détails de la réclamation (ID: " + ID + ")");
        lblDetails.setHorizontalAlignment(SwingConstants.CENTER);
        lblDetails.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblDetails.setBounds(242, 115, 300, 30);
        contentPane.add(lblDetails);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(219, 155, 360, 200);
        contentPane.add(scrollPane);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
        updateDetails(); // Met à jour les détails initiaux
        scrollPane.setViewportView(detailsArea);

        JButton btnRefuser = new JButton("Refuser");
        btnRefuser.setForeground(Color.WHITE);
        btnRefuser.setBackground(new Color(255, 128, 128));
        btnRefuser.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnRefuser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateReclamationStatus(ID, "Refusée");
                btnRefuser.setEnabled(false);
                updateDetails(); // Actualise les détails après la mise à jour du statut
                dispose();
            }
        });
        btnRefuser.setBounds(279, 365, 120, 30);
        contentPane.add(btnRefuser);

        JButton btnAccepter = new JButton("Accepter");
        btnAccepter.setForeground(Color.WHITE);
        btnAccepter.setBackground(new Color(255, 128, 128));
        btnAccepter.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnAccepter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateReclamationStatus(ID, "Acceptée");
                updateResolutionDate(ID, new Date());
                btnRefuser.setEnabled(false);
                btnAccepter.setEnabled(false);
                updateDetails(); // Actualise les détails après la mise à jour du statut
                dispose();
            }
        });
        btnAccepter.setBounds(422, 365, 120, 30);
        contentPane.add(btnAccepter);

        JComboBox motif = new JComboBox();
        motif.setModel(new DefaultComboBoxModel(new String[]{"Motif de refus", "Documentation incomplète ou incorrecte", "Fraude ou fausse déclaration", "Responsabilité non prouvée", "Force majeure ou exclusion spécifique", "Responsabilité prouvée ", "Aucune exclusion spécifique ne s'applique", "Permession de démarche judicaire", "Autre"}));
        motif.setToolTipText("");
        motif.setBounds(314, 405, 200, 30);
        contentPane.add(motif);
    }

    private void updateReclamationStatus(int id, String newStatus) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionDB.getConnection();
            String sql = "UPDATE Reclamation SET status = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, newStatus);
            statement.setInt(2, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Status de la réclamation " + id + " mis à jour à : " + newStatus);
            } else {
                System.out.println("Aucune réclamation trouvée avec l'ID : " + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateResolutionDate(int id, Date currentDate) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionDB.getConnection();
            String sql = "UPDATE Reclamation SET date_resolution = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setDate(1, new java.sql.Date(currentDate.getTime()));
            statement.setInt(2, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Date de résolution de la réclamation " + id + " mise à jour à : " + currentDate);
            } else {
                System.out.println("Aucune réclamation trouvée avec l'ID : " + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Méthode pour mettre à jour les détails dans la zone de texte
    private void updateDetails() {
        detailsArea.setText(""); // Efface le contenu existant
        detailsArea.append("Nom: " + nom + "\n");
        detailsArea.append("Type: " + type + "\n");
        detailsArea.append("Localisation: " + localisation + "\n");
        detailsArea.append("Date de Réclamation: " + date_creation + "\n");
        detailsArea.append("Description: " + description + "\n");
        detailsArea.append("Status: " + status + "\n");
        detailsArea.append("CIN: " + CIN + "\n");
    }
}
