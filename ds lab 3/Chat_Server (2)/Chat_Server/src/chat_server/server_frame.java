//LAB #3
// Name: Prithul Bahukhandi
// UTA ID: 1001730554

package chat_server;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import javax.jms.MessageListener;

import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Date;

 // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
public class server_frame extends javax.swing.JFrame 
{
   ServerSocket serverSock;
   ArrayList clientOutputStreams;
   ArrayList<String> users;         // for storing list of online users
   ArrayList<String> allusers = new ArrayList();    // for storing all users log
   HashMap<String,PrintWriter> hmap= new HashMap<String,PrintWriter>(); // hashmap for storing printwriters and their username
   HashMap<String,Integer[]> hmap2=new HashMap<String,Integer[]>();
//   Integer[] sampleAr={0,0,0};
   boolean sample=false;
   

    // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
   public class ClientHandler implements Runnable	// this is thread running for every connecting client
   {
       BufferedReader reader;
       Socket sock;
       PrintWriter client;

       
       public ClientHandler(Socket clientSocket, PrintWriter user) // constructor for CLientHandler
       {
            client = user;
            
            try 
            {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            }
            catch (Exception ex) 
            {
                ta_chat.append("Unexpected error... \n");
            }

       }

       @Override
       public void run() 
       {
            String message, connect = "Connect", disconnect = "Disconnect", toAll = "All", dischat = "Dischat", send="Send" ;
            String[] data;
            Session session=null;
            Destination destination=null;
            

            try 
            {
                while ((message = reader.readLine()) != null) // reading the client input stream
                {
                    ta_chat.append("Received: " + message + "\n");  //displaying received message from client
                    data = message.split(":");
                    
//                    for (String token:data) 
//                    {
//                        ta_chat.append(token + "\n");
//                    }
                    System.out.println("data is "+data[0]);
                    if (data[2].equals(connect))    //if data[2] string equals connect then it will check if username is already present in online user or not
                    {
                        Thread.sleep(1000);
                        if(users.contains(data[0])){        // if user is already in online user list then prompt the user to enter another username as user is already present from another client
                            client= new PrintWriter(sock.getOutputStream());
                            client.println("username is used.. try another");
                            client.flush();
                            
                            
                        }
//                        else if(!data[0].equals("a") || !data[0].equals("b") || !data[0].equals("c")){
//                            client= new PrintWriter(sock.getOutputStream());
//                            client.println("Username must be a / b / c");
//                            client.flush();
//                        }
                        else if(data[0].equals("a") || data[0].equals("b") || data[0].equals("c")){
                        hmap2.put(data[0], new Integer[]{0,0,0});             
                        hmap.put(data[0], client); // if the user is not online from another client then  add the user to hashmap
                        userAdd(data[0]);   // add the user to online user list
                        allusersAdd(data[0]);   // add the user to all user list
                        tellEveryone((data[0] + ":" + data[1] + ":" + toAll +":" +allusers.toString())); // this method is used to display on all client gui that user is now connected
                        }
                        else{
                            client= new PrintWriter(sock.getOutputStream());
                            client.println("Username must be a / b / c");
                            client.flush();
                        }
                    } 
  
                    else if(data[2].equals("clock")){
                        ta_chat.append("vector clock received \n"); 
                        if(data[0].equals("a")){
                            Integer []sampleAr= hmap2.get("a");
                            sampleAr[0]=sampleAr[0]+1;
                            hmap2.put(data[0], sampleAr);
//                            System.out.println("message received from a \n");
//                            System.out.println("updated sampleAr= " +sampleAr[0]+sampleAr[1]+sampleAr[2]);
                        }
                        else if(data[0].equals("b")){
                            Integer []sampleAr= hmap2.get("b");
                            sampleAr[1]=sampleAr[1]+1;
                            hmap2.put(data[0], sampleAr);
//                            System.out.println("message received from b \n");
//                            System.out.println("updated sampleAr= " +sampleAr[0]+sampleAr[1]+sampleAr[2]);
                        }
                        else if(data[0].equals("c")){
                            Integer []sampleAr= hmap2.get("c");
                            sampleAr[2]=sampleAr[2]+1;
                            hmap2.put(data[0], sampleAr);
                        }
                                for(Integer[] x: hmap2.values()){
                                System.out.println(Arrays.toString(x));
                            }
                        
//                        hmap2.put(data[0], hmap2.get(data[0])+1);
                        String[] arrnew=new String[hmap2.size()-1];
                        int i=0;
                        for(String tok:hmap2.keySet()){
                            if(!tok.equals(data[0])){
                                arrnew[i]=tok;
                                i++;
                            }
                            else{
                                continue;
                            }
                        }
                        System.out.println(Arrays.toString(arrnew));
                        Object key = arrnew[new Random().nextInt(arrnew.length)];
//                        int maximum=(Math.max(hmap2.get(data[0]),hmap2.get(key.toString())))+1;
//                        hmap2.put(key.toString(), maximum);
//                        sendVectorClock(data[0],key.toString(),client);
//                        System.out.println("keyyya " +key.toString());
                        if(key.toString().equals("a")){
                            Integer[] cmp1=hmap2.get(data[0]);
                            Integer[] cmp2=hmap2.get("a");
                            System.out.println("here are the cmp1 values before "+"of "+key.toString() +" "+Arrays.toString(cmp1));
                            System.out.println("here are the cmp2 values before "+"of "+key.toString() +" "+Arrays.toString(cmp2));
                            cmp2[0]=cmp2[0]+1;
                            cmp2[1]=Math.max(cmp2[1], cmp1[1]);
                            cmp2[2]=Math.max(cmp2[2], cmp1[2]);
                            System.out.println("here are the cmp1 values after "+"of "+key.toString() +" "+Arrays.toString(cmp1));
                            System.out.println("here are the cmp2 values after "+"of "+key.toString() +" "+Arrays.toString(cmp2));
                            hmap2.put(key.toString(), cmp2);
                        }
                        else if(key.toString().equals("b")){
                            Integer[] cmp1=hmap2.get(data[0]);
                            Integer[] cmp2=hmap2.get("b");
                            cmp2[1]=cmp2[1]+1;
                            cmp2[0]=Math.max(cmp2[0], cmp1[0]);
                            cmp2[2]=Math.max(cmp2[2], cmp1[2]);
                            hmap2.put(key.toString(), cmp2);
                        }
                        else if(key.toString().equals("c")){
                            Integer[] cmp1=hmap2.get(data[0]);
                            Integer[] cmp2=hmap2.get("c");
                            cmp2[2]=cmp2[2]+1;
                            cmp2[0]=Math.max(cmp2[0], cmp1[0]);
                            cmp2[1]=Math.max(cmp2[1], cmp1[1]);
                            hmap2.put(key.toString(), cmp2);
                        }
                        
                        sendVectorClock(data[0],key.toString(),client);
                    }
                                 
                    else 
                    {
                        ta_chat.append("No Conditions were met. \n");
                    }
                } 
             } 
             catch (Exception ex) 
             {
                ta_chat.append("Lost a connection. \n");
                ex.printStackTrace();
                clientOutputStreams.remove(client);
             } 
	} 
    }
  
   
   // this class is used  for reading all the messages in  the queue
   public class ConsumerMessageListener implements MessageListener{
       private String consumerName;
       PrintWriter client;
       
       public ConsumerMessageListener(String consumerName, PrintWriter client){
           this.consumerName=consumerName;
           this.client=client;
           Date dateTime;     
       }
       public void onMessage(Message message){
           sample=true;
           TextMessage textMessage=(TextMessage) message;
            try {
            System.out.println(consumerName + " received " + textMessage.getText());
            Date dateTime = new java.util.Date(message.getJMSTimestamp()); // storing TimeStamp in Data object type
            displayMsg(textMessage.getText(),client,dateTime); //passing the textmessage , and timestamp to client 
            
        } catch (JMSException e) {
            e.printStackTrace();
        }
       }
   }

    public server_frame() 
    {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        b_start = new javax.swing.JButton();
        b_end = new javax.swing.JButton();
        b_users = new javax.swing.JButton();
        b_clear = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        all_log = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Server's frame");
        setName("server"); // NOI18N
        setResizable(false);

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        b_start.setText("START");
        b_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_startActionPerformed(evt);
            }
        });

        b_end.setText("END");
        b_end.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_endActionPerformed(evt);
            }
        });

        b_users.setText("Online Users");
        b_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_usersActionPerformed(evt);
            }
        });

        b_clear.setText("Clear");
        b_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_clearActionPerformed(evt);
            }
        });

        all_log.setColumns(20);
        all_log.setRows(5);
        jScrollPane2.setViewportView(all_log);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Status");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setText("All Users");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_end, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_start, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_users, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_start)
                    .addComponent(b_users))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_clear)
                    .addComponent(b_end))
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
// https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    private void b_endActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_endActionPerformed
        try 
        {
            
            Thread.sleep(5000);                 //5000 milliseconds is five second.
            
        } 
        catch(InterruptedException ex) {Thread.currentThread().interrupt();}
        try{
        serverSock.close();}catch(Exception ex){};
        System.out.println(allusers.toString());
        broadcast("Server:disconnecting :shutdown\n");
        ta_chat.append("Server stopped... \n");
        
        
        ta_chat.setText("");
    }//GEN-LAST:event_b_endActionPerformed
 // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    private void b_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_startActionPerformed
        Thread starter = new Thread(new ServerStart());
        starter.start();
        
        ta_chat.append("Server started...\n");
    }//GEN-LAST:event_b_startActionPerformed
 // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    private void b_usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_usersActionPerformed
        ta_chat.append("Online users : \n");
        for (String current_user : users)
        {
            ta_chat.append(current_user);
            ta_chat.append("\n");
        }    
        
    }//GEN-LAST:event_b_usersActionPerformed
 // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    private void b_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_clearActionPerformed
        ta_chat.setText("");
    }//GEN-LAST:event_b_clearActionPerformed

     // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                new server_frame().setVisible(true);
            }
        });
    }
    
    public class ServerStart implements Runnable 
    {
        @Override
        public void run() 
        {
            clientOutputStreams = new ArrayList();
            users = new ArrayList();  
            System.out.println(allusers.toString());
            
            
            try 
            {


                
                serverSock = new ServerSocket(2222);

                while (true) 
                {
				Socket clientSock = serverSock.accept(); // server accept client connection request 
				PrintWriter writer = new PrintWriter(clientSock.getOutputStream()); // open a writer for this client
				clientOutputStreams.add(writer);    // add the writer to  the list of all client writers
				Thread listener = new Thread(new ClientHandler(clientSock, writer)); // spawn a thread for all connecting clients
				listener.start();   // starting the thread
				ta_chat.append("Got a connection. \n");
                                
                }
            }
            catch (Exception ex)
            {
                ta_chat.append("Error making a connection. \n");
            }
        }
    }
     // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    public void userAdd (String data) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        users.add(name);
    }
    public void allusersAdd (String data)
    {// this method adds the user to allusers list
        if(!allusers.contains(data)){
        allusers.add(data);
        String[] tempList=new String[allusers.size()]; // modifying the size of list
        allusers.toArray(tempList);
        all_log.setText("");
        for (String token: tempList)
        {
            all_log.append(token +"\n");    // displaying the alluser log on servers gui
        }
        }
        
    }
     // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    // whenver user disconnects the user has to removed from online user list
    public void userRemove (String data) 
    {
        String message, add = ": :Connect", done = "Server: :Done", name = data;
        users.remove(name); //the user will be removed from online user list

    }
     // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    public void tellEveryone(String message) 
    {
            try 
            {
                for(int i=0;i<clientOutputStreams.size();i++){
                PrintWriter w=(PrintWriter) clientOutputStreams.get(i);
		w.println("passing:strr:empty");
                w.flush();
                w.println(message);
                w.flush();
		ta_chat.append("Sending: " + message + "\n"); 
                ta_chat.setCaretPosition(ta_chat.getDocument().getLength());}
                
            } 
            catch (Exception ex) 
            {
		ta_chat.append("Error telling everyone. \n");
            }
         
    }
    public void sending(String d0, String d1, String d2,String message, PrintWriter client){
        String[] chunks= message.split(";");
//        ta_chat.append("i am here \n");
//        System.out.println(hmap.keySet());
//        System.out.println(hmap.values());
        for(String tok: chunks){
            if(hmap.containsKey(tok))
            {
                PrintWriter w=hmap.get(tok);
                w.println(d0 +":" +d1 +":" +d2 +":" +tok);
                w.flush();
                client.println(d0 +":" +d1 +":" +d2 +":" +tok);
                ta_chat.append("Sending: " + d1 + ":" +tok +"\n");
                client.flush();
            }
            else{
                String off="offline";
                client.println(d0 +":" +d1 +":" +off +":" +tok);
                ta_chat.append("Sending: " + d1 + ":" +tok +": Failed,user not present.\n");
                client.flush();
            }
        }
    }
     // https://drive.google.com/drive/u/0/folders/0B4fPeBZJ1d19WkR3blE4ZVNTams   
    public void broadcast(String message){
        System.out.println("broadcasting");
                    try 
            {
                for(int i=0;i<clientOutputStreams.size();i++){
                PrintWriter w=(PrintWriter) clientOutputStreams.get(i);
		w.println("passing:strr:empty");
                w.flush();
                w.println(message);
                w.flush();
//		ta_chat.append("Sending: " + message + "\n"); 
//              ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
                }

            } 
            catch (Exception ex) 
            {
		ta_chat.append("Error telling everyone. \n");
            }
    }
    
    public void displayMsg(String test, PrintWriter writer, Date dateTime ){
        try{
            System.out.println("testing");
            System.out.println(dateTime);
        writer.println("Sending:msg:text:" +test +":" +dateTime);
        writer.flush();}
        catch(Exception ex){System.out.println("not working");};
    }
    public void sendToOnline(String d0, String d1, String d2 , PrintWriter client){
        try{
            PrintWriter w=hmap.get(d2);
            ta_chat.append("Sending:" +d1 +" from " +d0 +" to " +d2 +"\n");
            w.println(d0 +":"+d1 +":sendToOnline"+":" +d2);
            w.flush();
            ta_chat.append("Sending:" +d1 +" from " +d0 +" to " +d2 +"\n");
            client.println(d0 +":"+d1 +":sendToOnline"+":" +d2);
            client.flush();
            
        }catch(Exception ex){System.out.println("not sending");}
    }
    
    public void sendVectorClock(String d0, String d1, PrintWriter client){
            try{
                ta_chat.append("printing vector clocks \n");
                client.println(d0 +":"+d1 +":vectorclock1"+":" +Arrays.toString(hmap2.get(d0))+":"+Arrays.toString(hmap2.get(d1)));
                client.flush();
                PrintWriter w=hmap.get(d1);
                ta_chat.append("printing vector clocks \n");
                w.println(d0 +":"+d1 +":vectorclock2"+":" +Arrays.toString(hmap2.get(d0))+":"+Arrays.toString(hmap2.get(d1)));
                w.flush();
        }catch(Exception ex){System.out.println("not sending");}
    }
    public void sendQueueEmptyMsg(PrintWriter client){
        client.println("Queue:is:emp:msg");
        client.flush();
    }
    
    public void MsgToQue(PrintWriter client, String tok){
        client.println("User:"+tok+":"+"msgToQueue");
        client.flush();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea all_log;
    private javax.swing.JButton b_clear;
    private javax.swing.JButton b_end;
    private javax.swing.JButton b_start;
    private javax.swing.JButton b_users;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea ta_chat;
    // End of variables declaration//GEN-END:variables
}
