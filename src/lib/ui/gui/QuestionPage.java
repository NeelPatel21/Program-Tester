package lib.ui.gui;
import javax.swing.*;
import lib.dT.problemAdder.IntProgramDetail;

/**
 *
 * @author Parth Doshi
 */
public class QuestionPage extends JFrame{
     JLabel pid;
     JLabel title;
     JLabel description;
     JLabel input;
     JLabel output;
     JPanel saminput;
     JPanel samoutput;
     IntProgramDetail det;
     public QuestionPage() {    
        super("Question Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        pid=new JLabel(""+det.getProgramID());    //Converted to String from Long
        title=new JLabel(""+det.getTitle());
        description=new JLabel(""+det.getDescription());
        input=new JLabel(""+det.getInput());
        output=new JLabel(""+det.getOutput());
        saminput=new JPanel();
        samoutput=new JPanel();
        setVisible(true);
        this.add(pid);
        this.add(title);
        this.add(description);
        this.add(input);
        this.add(output);
        
     }
     
     
     public static void main(String[] args){
        ResultPage rp=new ResultPage();
    }
}