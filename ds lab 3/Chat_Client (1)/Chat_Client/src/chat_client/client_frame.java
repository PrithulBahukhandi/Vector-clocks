// LAB #3
// Name: Prithul Bahukhandi
// UTA ID: 1001730554

package chat_client;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.DefaultListModel;


// https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams


public class client_frame extends javax.swing.JFrame 
{
    String username, address = "localhost";
    ArrayList<String> users = new ArrayList();      // this list is for storing online users
    ArrayList<String> allusers=new ArrayList();     // this list is for storing log of all users
    int port = 2222;
    Boolean isConnected = false;
    int checker=1;
    
    Socket sock;
    BufferedReader reader;
    PrintWriter writer;     
    String str;
    DefaultListModel dm=new DefaultListModel();         // this will display a list on the client gui
     t tim= new t();
    
    
    //--------------------------//

//  https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams    
    public void ListenThread() 
    {
         Thread IncomingReader = new Thread(new IncomingReader());      // start thread for all connecting users
         IncomingReader.start();
    }
    public void timeRun(){
       
        tim.start();
        
        
    }
    
    //--------------------------//
//  https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams    
    //this method adds the user in the users list  if the user is not present in the all user list
    public void userAdd(String data) 
    {
        String[] s=data.split(","); 
        for(String tok: s){
        if(!users.contains(tok))
        {
            users.add(tok);         //adding the user to array list of online users
        }

        }
    }
//   https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    // this method is for adding all the users in the list and displaying it on the gui of client
    public void userListAdd(){
        String[] temp= new String[users.size()];    //modifying the size of list
         users.toArray(temp);
         dm.clear();
         
         for(String token: temp)
         {
             dm.addElement(token);      // adding the user to the list dm
         }
    }
    

    
    //--------------------------//
 // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    public void userRemove(String data) 
    {
         ta_chat.append(data + " is now offline.\n"); //
    }
    
    //--------------------------//
    
    public void writeUsers() 
    {
         String[] tempList = new String[(users.size())];
         users.toArray(tempList);
         for (String token:tempList) 
         {
             //users.append(token + "\n");
         }
    }
    
    //--------------------------//
//  https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    public void sendDisconnect() 
    {
        String bye = (username + ": :Disconnect");
        try
        {
            ta_chat.append(username +": has disconnected. \n");
            writer.println(bye); 
            writer.flush();
            
        } catch (Exception e) 
        {
            ta_chat.append("Could not send Disconnect message.\n");
        }
    }

    //--------------------------//
 
 //  https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams
    public void Disconnect() 
    {
        try 
        {
//            ta_chat.append("Disconnected.\n");
            sock.close();
        } catch(Exception ex) {
            ta_chat.append("Failed to disconnect. \n");
        }
        
        tf_username.setEditable(true);
        b_connect.setEnabled(true);

    }
    
    public client_frame() 
    {
        initComponents();
    }
    
    // https://www.youtube.com/watch?v=36jbBSQd3eU
    // this is a timer that runs clock every second
    
    public class t {
        int secPass=0;
        Timer myTimer= new Timer();
        Random r = new Random();
        int a=0;
        int rand = r.nextInt(9) + 2; // generate a starting random number when client connects
        TimerTask task= new TimerTask(){
        public void run(){
            secPass++; // increment the counter for every clock tick
            timerCounter.setText(Integer.toString(secPass));
            if(rand==secPass-a){
             
             a=secPass;
             rand = r.nextInt(9) + 2;  // generates a random number between 2 and 10
             writer.println(username +":vector:clock"); //sends an implicit message to the server
             writer.flush();
            }
        }
        };
        public void start(){
            myTimer.scheduleAtFixedRate(task,1000,1000); // every 1 second this task gets excted
        }
    }
    
    //--------------------------//
    
    public class IncomingReader implements Runnable
    {
        @Override
        public void run() 
        {
//            System.out.println("debug1");
            String[] data;
            String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", toAll="All", dischat = "Dischat",send="Send",offline="offline";

            try 
            {
                while ((stream = reader.readLine()) != null) // reading input stream of the client
                {   
                     data = stream.split(":");      // split the string and compare data[2], different operations will be performed based on data[2]
//                     System.out.println(data[0] +" " +data[1]);
                    
                     if (data[2].equals("vectorclock1")){
                        ta_chat.append("\nMessage send \n");
                        ta_chat.append("Updated vector clock of: " +data[0] +"= "+data[3]+"\n");
                        ta_chat.append("Intended recipient: " +data[1]+"\n");
                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());  
                     }
                        else if (data[2].equals("vectorclock2")){
                        ta_chat.append("\nMessage receive \n");
                        ta_chat.append("Received clock from: " +data[0] +"= "+data[3] +"\n");
                        ta_chat.append("Updated vector clock of: " +data[1] +"= "+data[4]+"\n");
                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());  
                     }
                     else if(data[2].equals("empty")){}
                }
           }catch(Exception ex) { System.out.println("Message not sent");}
        }
    }

    //--------------------------//
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        lb_address = new javax.swing.JLabel();
        tf_address = new javax.swing.JTextField();
        lb_username = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        b_connect = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        timerCounter = new javax.swing.JTextField();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Client's frame");
        setName("client"); // NOI18N
        setResizable(false);

        lb_address.setText("Address : ");

        tf_address.setText("localhost");
        tf_address.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_addressActionPerformed(evt);
            }
        });

        lb_username.setText("Username :");

        tf_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_usernameActionPerformed(evt);
            }
        });

        b_connect.setText("Connect");
        b_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_connectActionPerformed(evt);
            }
        });

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        jLabel2.setText("Status");

        jLabel4.setText("Client clock :");

        timerCounter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timerCounterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lb_address, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lb_username, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tf_username)
                                    .addComponent(tf_address, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(timerCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(39, 39, 39)
                                    .addComponent(b_connect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_address)
                    .addComponent(tf_address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tf_username, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_username))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(timerCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_connect))
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tf_addressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_addressActionPerformed
       
    }//GEN-LAST:event_tf_addressActionPerformed

    private void tf_usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_usernameActionPerformed
    
    }//GEN-LAST:event_tf_usernameActionPerformed

    private void b_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_connectActionPerformed
            // this method gets invoked whenever connect button is pressed'
            username = tf_username.getText();       //get the username from the field

            try 
            {
                //client request a connection from the server, server will check if the user is already online or not
                //if user is online from another client then it will give error and ask to enter different username
                //else the user will get connected
                sock = new Socket(address, port); // requesting connection  
                InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamreader);
                writer = new PrintWriter(sock.getOutputStream());
                writer.println(username + ":has connected.:Connect"); // passing to client output stream
                writer.flush(); 
                String incoming= reader.readLine();
                if(incoming.equals("username is used.. try another")){ // error if user is already connected will be shown on client gui
                ta_chat.append(incoming +"\n");
                sock.close();
                }
                else if(incoming.equals("Username must be a / b / c")){ // if user is not a/b/c then server does not connect
                ta_chat.append(incoming +"\n");
                sock.close();
                }
                else{
//                    System.out.println(users.toString());
//                    userAdd(username);
//                    ta_chat.append(username +": has connected. \n");
                    ta_chat.append(username +": has connected \n");
                    tf_username.setEditable(false);
                    b_connect.setEnabled(false);  
                    timeRun();
                }
                
            } 
            catch (Exception ex) 
            {
                ta_chat.append("Cannot Connect! Try Again. \n");
                tf_username.setEditable(true);
            }
            ListenThread();
            
            
        
    }//GEN-LAST:event_b_connectActionPerformed

    private void timerCounterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timerCounterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timerCounterActionPerformed

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new client_frame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_connect;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lb_address;
    private javax.swing.JLabel lb_username;
    private javax.swing.JTextArea ta_chat;
    private javax.swing.JTextField tf_address;
    private javax.swing.JTextField tf_username;
    private javax.swing.JTextField timerCounter;
    // End of variables declaration//GEN-END:variables
}
