package com.saylorsolutions.devutils.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JTable;

public class App {

	private JFrame frmDevutils;
	private JTextField fileInputTextField;
	private JButton btnFileOutput;
	private String stringEncoding = "UTF-8";
	private JTextArea stringInputTextArea;
	private static final String HEX_FORMAT = "0x%02x";
	private JTextArea fileOutputTextArea;
	private JTextArea stringOutputTextArea;
	private JTable csvTable;
	private JScrollPane csvTableScrollPane;
	private JPanel csvPanel;
	private JLabel csvStatusLabel = new JLabel("Status: Nothing Open");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frmDevutils.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDevutils = new JFrame();
		frmDevutils.setIconImage(Toolkit.getDefaultToolkit().getImage(App.class.getResource("/com/saylorsolutions/devutils/resources/favicon.png")));
		frmDevutils.setResizable(false);
		frmDevutils.setTitle("DevUtils");
		frmDevutils.setBounds(100, 100, 670, 450);
		frmDevutils.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmDevutils.setJMenuBar(menuBar);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmDevutils.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel stringArrayTab = new JPanel();
		tabbedPane.addTab("String -> Array", null, stringArrayTab, null);
		stringArrayTab.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		stringArrayTab.add(splitPane);
		
		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JButton btnGenerateArray = new JButton("Generate Array");
		btnGenerateArray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(stringInputTextArea.getText().length() == 0) return;
				StringBuilder sb = new StringBuilder();
				sb.append("byte[] bytes = new byte[] {");
				try {
					byte[] bytes = stringInputTextArea.getText().getBytes(stringEncoding);
					sb.append(String.format(HEX_FORMAT, bytes[0]));
					for(int ii = 1; ii < bytes.length; ii++) {
						sb.append(String.format("," + HEX_FORMAT, bytes[ii]));
					}
					sb.append("};");
					stringOutputTextArea.setText(sb.toString());
				} catch (UnsupportedEncodingException uex) {
					showExceptionPopup(uex);
				}
			}
		});
		panel_2.add(btnGenerateArray);
		
		JRadioButton rdbtnUtf8 = new JRadioButton("UTF-8");
		rdbtnUtf8.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					stringEncoding = "UTF-8";
				}
			}
		});
		rdbtnUtf8.setSelected(true);
		panel_2.add(rdbtnUtf8);
		
		JRadioButton rdbtnUtf16 = new JRadioButton("UTF-16");
		rdbtnUtf16.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					stringEncoding = "UTF-16";
				}
			}
		});
		panel_2.add(rdbtnUtf16);
		
		ButtonGroup encodingGroup = new ButtonGroup();
		encodingGroup.add(rdbtnUtf8);
		encodingGroup.add(rdbtnUtf16);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stringInputTextArea.setText("");
				stringOutputTextArea.setText("");
			}
		});
		panel_2.add(btnReset);
		
		stringInputTextArea = new JTextArea();
		stringInputTextArea.setWrapStyleWord(true);
		stringInputTextArea.setLineWrap(true);
		panel_1.add(stringInputTextArea, BorderLayout.CENTER);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		
		stringOutputTextArea = new JTextArea();
		stringOutputTextArea.setWrapStyleWord(true);
		stringOutputTextArea.setLineWrap(true);
		scrollPane_1.setViewportView(stringOutputTextArea);
		
		JPanel fileArrayTab = new JPanel();
		tabbedPane.addTab("File -> Array", null, fileArrayTab, null);
		fileArrayTab.setLayout(new BorderLayout(0, 0));
		
		JSplitPane fileSelectSplitPane = new JSplitPane();
		fileSelectSplitPane.setResizeWeight(0.4);
		fileSelectSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		fileArrayTab.add(fileSelectSplitPane, BorderLayout.CENTER);
		
		JPanel fileSelectPanel = new JPanel();
		fileSelectSplitPane.setLeftComponent(fileSelectPanel);
		fileSelectPanel.setLayout(null);
		
		JLabel lblSelectFile = new JLabel("Select browse or drag and drop to set the file source");
		lblSelectFile.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectFile.setBounds(10, 11, 637, 14);
		fileSelectPanel.add(lblSelectFile);
		
		fileInputTextField = new JTextField();
		fileInputTextField.setBounds(10, 36, 538, 20);
		fileSelectPanel.add(fileInputTextField);
		fileInputTextField.setColumns(10);
		fileInputTextField.setDropTarget(new DropTarget() {
			@Override
			public synchronized void drop(DropTargetDropEvent dtde) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY);
				@SuppressWarnings("unchecked")
				List<File> droppedFiles;
				try {
					droppedFiles = (List<File>)dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					fileInputTextField.setText(droppedFiles.get(0).getAbsolutePath());
				} catch (UnsupportedFlavorException | IOException ex) {
					showExceptionPopup(ex);
				}
			}
		});
		fileInputTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				btnFileOutput.setEnabled(true);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				checkOutputButton();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				checkOutputButton();
			}
			private void checkOutputButton() {
				if(fileInputTextField.getText().length() == 0) {
					btnFileOutput.setEnabled(false);
				} else {
					btnFileOutput.setEnabled(true);
				}
			}
		});
		
		JButton btnFileBrowse = new JButton("Browse");
		btnFileBrowse.addActionListener(new ActionListener() {
			JFileChooser fc = new JFileChooser();
			public void actionPerformed(ActionEvent e) {
				int choice = fc.showOpenDialog(frmDevutils);
				if(choice == JFileChooser.APPROVE_OPTION) {
					fileInputTextField.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnFileBrowse.setBounds(558, 35, 89, 23);
		fileSelectPanel.add(btnFileBrowse);
		
		btnFileOutput = new JButton("Output");
		btnFileOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File input = new File(fileInputTextField.getText());
				try(FileInputStream fis = new FileInputStream(input);) {
					StringBuilder sb = new StringBuilder();
					sb.append("byte[] bytes = new byte[] {");
					long fsize = Files.size(input.toPath());
					if (fsize > 0) {
						byte b = (byte) fis.read();
						sb.append(String.format(HEX_FORMAT, b));
						for(long ii = 1; ii < fsize; ii++) {
							sb.append(String.format(","+HEX_FORMAT, (byte)fis.read()));
						}
						sb.append("};");
						fileOutputTextArea.setText(sb.toString());
					} else {
						fileOutputTextArea.setText("byte[] bytes = new byte[0];");
						showAppMessage("File is empty");
					}
				} catch (IOException ex) {
					showExceptionPopup(ex);
				}
			}
		});
		btnFileOutput.setEnabled(false);
		btnFileOutput.setBounds(10, 67, 89, 23);
		fileSelectPanel.add(btnFileOutput);
		
		JButton btnReset_1 = new JButton("Reset");
		btnReset_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileInputTextField.setText("");
				fileOutputTextArea.setText("");
			}
		});
		btnReset_1.setBounds(109, 67, 89, 23);
		fileSelectPanel.add(btnReset_1);
		
		JScrollPane scrollPane = new JScrollPane();
		fileSelectSplitPane.setRightComponent(scrollPane);
		
		fileOutputTextArea = new JTextArea();
		fileOutputTextArea.setWrapStyleWord(true);
		fileOutputTextArea.setLineWrap(true);
		scrollPane.setViewportView(fileOutputTextArea);
		
		csvPanel = new JPanel();
		tabbedPane.addTab("CSV Editor", null, csvPanel, null);
		csvPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		csvPanel.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar_1 = new JMenuBar();
		panel.add(menuBar_1);
		
		JMenu mnFile = new JMenu("File");
		menuBar_1.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCsvTable(new JTable(20,10));
			}
		});
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open...");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmSaveAs);
		
		csvTableScrollPane = new JScrollPane();
		csvPanel.add(csvTableScrollPane, BorderLayout.CENTER);
		JPanel csvStatusPanel = new JPanel();
		csvPanel.add(csvStatusPanel, BorderLayout.PAGE_END);
		csvStatusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		csvStatusPanel.add(csvStatusLabel);
		
//		setCsvTable(new JTable());
	}

	private void setCsvTable(JTable table) {
		EventQueue.invokeLater(() -> {
			csvPanel.remove(csvTableScrollPane);
			csvTable = table;
			csvTableScrollPane = new JScrollPane(csvTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			csvTable.setFillsViewportHeight(true);
			csvPanel.add(csvTableScrollPane, BorderLayout.CENTER);
			csvStatusLabel.setText("Status: New CSV file");
		});
	}

	public static final void showExceptionPopup(Exception ex) {
		JOptionPane.showMessageDialog(null, "Exception thrown: " + ex.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE + JOptionPane.OK_OPTION);
	}

	public static final void showAppMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "App Message", JOptionPane.INFORMATION_MESSAGE + JOptionPane.OK_OPTION);
	}
}
