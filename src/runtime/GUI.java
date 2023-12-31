package runtime;

//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GUI {

    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu menu1, menu2;
    private JMenuItem menu11, menu12, menu22;
    private JPanel panel;
    private JLabel label;
    private JTextField textField; //chỗ nhập tin nhắn
    private JButton btnSend, btnReset;
    private JTextArea textArea; //khung giữa

    private String tmp = ""; //hứng giá trị từ file
    private String url = "";

    public GUI() {
        //Creating the Frame

        frame = new JFrame("Chat Frame");

        //Creating the MenuBar and adding components
        menuBar = new JMenuBar();

        menu1 = new JMenu("FILE");

        menu2 = new JMenu("Help");

        menuBar.add(menu1);

        menuBar.add(menu2);

        menu11 = new JMenuItem("Open");

        menu12 = new JMenuItem("Save as");

        menu22 = new JMenuItem("About me");

        menu1.add(menu11); //File -> Open

        menu1.add(menu12); //File -> Save as

        menu2.add(menu22); //Help -> About me

        //Creating the panel at bottom and adding components
        panel = new JPanel(new FlowLayout()); //nên áp cái layout của nó từ đầu, dễ kiểm soát hơn
        //FlowLayout là trình quản lý bố cục mặc định cho mọi JPanel. Nó lần lượt đưa ra các thành phần trong một hàng duy nhất.

        label = new JLabel("Enter Text");

        textField = new JTextField(10); // accepts upto 10 characters

        btnSend = new JButton("Send");

        btnReset = new JButton("Reset");

        panel.add(label); // Components Added using Flow Layout

        panel.add(textField);

        panel.add(btnSend);

        panel.add(btnReset);

        // Text Area at the Center
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false); //make read-only JTextArea

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel); //bottom chứa panel: textField, btnSend, btnReset

        frame.getContentPane().add(BorderLayout.NORTH, menuBar); //top chứa menuBar: File, HELPS

        frame.getContentPane().add(BorderLayout.CENTER, scrollPane); //giữa chứa text Area

        //sự kiện
        btnSend.addActionListener(new ClickSend());
        btnReset.addActionListener(new ClickReset());
        textField.addKeyListener(new PressEnter());
        menu11.addActionListener(new OpenFile());

        //Save as: ghi ra một file gì đó
        menu12.addActionListener(new SaveFile());

        //aboutme
        menu22.addActionListener(new AboutMe());

        //---------------------------------------------------------------------------------------------------------
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setPreferredSize(frame.getSize());
        frame.pack();
        frame.setVisible(true);
    }

    private String getDateTime() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());

        return nowAsISO;
    }

    //bấm send là trên app sẽ in ra dòng mình nhập trong textField
    private class ClickSend implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //tạo 1 label rỗng
//            tmp.setText(textField.getText());
//            JOptionPane.showMessageDialog(null, tmp.getText());
            btnReset.setEnabled(true);
            String time = getDateTime();
            textArea.append(textField.getText() + " Sended at: " + time + "\n");
            textField.setText("");
        }

    }

    private class ClickReset implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //xóa đi trạng thái clickable của nut Reset
            if (!textArea.getText().equals("")) {

                textArea.setText("");
            } else {
                btnReset.setEnabled(false);
                JOptionPane.showMessageDialog(null, "Nothing to reset");
            }
        }

    }

    private class PressEnter implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            btnReset.setEnabled(true);
        }

        @Override
        public void keyPressed(KeyEvent e) {
//            System.out.println("Key pressed code=" + e.getKeyCode() + ", char=" + e.getKeyChar());
            //\n code = 10
            if (e.getKeyCode() == 10) {
                String time = getDateTime();
                textArea.append(textField.getText() + " Sended at: " + time + "\n");
                textField.setText("");
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }

    private class OpenFile implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
//            JOptionPane.showMessageDialog(null, "You must choose the .txt file");

//                System.out.println("You must choose the .txt file"); //msg
            JFileChooser fileChooser = new JFileChooser();

            int returnValue = fileChooser.showOpenDialog(null);

//                if (returnValue == JFileChooser.APPROVE_OPTION) {
//                    File selectedFile = fileChooser.getSelectedFile();
//                    System.out.println(java.awt.Desktop.getDesktop().toString());
////                    try {
//////                        java.awt.Desktop.getDesktop().open(f);//<-- here
////                        
////                    } catch (IOException ex) {
////                        System.out.println("Loi roi");
////                    }
//                }
//                System.out.println(chooser.getDescription(f)); //trả ra tên ok //xíu xử lí lỗi chỗ này
//                if (f.getName().contains("txt")) {
//                    try {
//                        //tạo 1 hàm đọc file và in ra console
//                        
//                        readFile(f.getPath());
//                    } catch (Exception ex) {
//                        System.out.println("Loi roi");
//                    }
//                }
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                url = selectedFile.getAbsolutePath();

                try {
                    System.out.println("File Content: ");
                    tmp = readFile(url);
//                        JOptionPane.showMessageDialog(null, "File Read Successfully");
                    textArea.append(tmp);
                    btnReset.setEnabled(true);
                } catch (Exception ex) {
                    System.out.println("File loi roi");
                }

            }
        }
    }

    private String readFile(String url) throws Exception {
        BufferedReader bufferedReader = null;
        String tmp = "";
        try {
            Reader reader = new FileReader(new File(url));
            bufferedReader = new BufferedReader(reader);

            String str;
            while ((str = bufferedReader.readLine()) != null) {
                tmp = tmp.concat(str).concat("\n");
                System.out.println(str);
            }

            System.out.println("File Read Successfully");

        } catch (Exception e) {
            throw new Exception("File loi roi");
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return tmp;
    }

    //nâng cấp thêm tính năng
    //chọn directory nơi lưu file
    private boolean writeFile(String url) throws IOException {
        //nếu người dùng chưa bấm Open -> chưa mở file thì dữ liệu không truyền lên được 2 biến này
        if (this.url.equals("") || this.tmp.equals("")) {

            return false;
        }
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        String urlOut = "D:\\PERSONAL\\java\\GUI\\03-ChatFrame\\src\\output\\openFile.txt";
        try {
            Reader reader = new FileReader(new File(url));
            bufferedReader = new BufferedReader(reader);

            Writer writer = new FileWriter(new File(urlOut));
            bufferedWriter = new BufferedWriter(writer);

            String str;
            while ((str = bufferedReader.readLine()) != null) {
                bufferedWriter.write(str);
            }

            writer.flush();//đợi save xong mới đóng
            JOptionPane.showMessageDialog(null, "File Writed Successfull At: " + urlOut);
            return true;
        } catch (Exception e) {
            System.out.println("Loi save file" + e);
            return false;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    //hành động write file này khi ta nhập thông tin vào textField và nhấn send
    private boolean writeFile() throws IOException {
        String str = textArea.getText();

        System.out.println("Noi dung cua content: " + str);

        if (str.equals("")) {
            JOptionPane.showMessageDialog(null, "Nothing to save, Enter Text first");
            return false;
        }
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        String urlOut = "D:\\PERSONAL\\java\\GUI\\03-ChatFrame\\src\\output\\pressSend.txt";

        try {

            Reader reader = new StringReader(str);

            Writer writer = new FileWriter(new File(urlOut));

            bufferedReader = new BufferedReader(reader);

            bufferedWriter = new BufferedWriter(writer);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                writer.write(line);
                writer.write("\n");
            }
            writer.flush();//đợi save xong mới đóng
            JOptionPane.showMessageDialog(null, "File Writed Successfull At: " + urlOut);
            return true;
        } catch (Exception e) {
            System.out.println("Loi doc content Text Area");
            return false;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

        }

    }

    //save as: ghi file
    //có 2 trường hợp xảy ra
    //1. mình load file vô rồi sẽ ghi ra -> nếu readFile sẽ có dữ liệu trên tmp và path: url, đơm vô cái url này ghi ra một file ở destination nào đó (cố định)
    //2. mình tạo một đoạn bằng send .. rồi ghi ra -> collect dữ liệu trong ta, cầm cái đó ghi ra một file.txt (cái này không có url) mình sẽ dùng overload
    private class SaveFile implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String path = url;
                boolean isNotTextArea = textArea.getText().equals(""); //file này trống
                boolean isNotPath = path.equals(""); //path này trống
                if (isNotTextArea && isNotPath) {
                    JOptionPane.showMessageDialog(null, "You need to Open File or Enter Text first!");
                    //có 2 trường hợp, 1 là phải open file - 2 là viết gì đó vô
                } else if (isNotPath && !isNotTextArea) {
                    //nếu ta đọc file thì cũng quăng vô ta ->
                    //send -> quăng vô ta, nó sẽ bắt thằng này trước -> không ổn
                    //cách khác để xét: khi send ta không có url, và open file thì sẽ có url để identifier
                    writeFile();
                } else if (!isNotPath && !isNotTextArea) {
                    writeFile(path);
                }

            } catch (IOException ie) {
                System.out.println("Loi ghi file o Save File");
            }

        }

    }

    private class AboutMe implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "SIMPLE CHAT FRAME\n"
                    + "This is my first simple project when learning Java Swing, hope you guys enjoy ^^\n"
                    + "Author: lcaohoanq\n"
                    + "Github: https://github.com/lcaohoanq");
        }
    }

    public static void main(String[] args) {
        GUI program = new GUI();
    }

}
