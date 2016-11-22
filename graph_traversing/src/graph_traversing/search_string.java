
package graph_traversing;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.impl.util.StringLogger;

public class search_string extends javax.swing.JFrame
{
    

    
    public search_string() {initComponents(); }
    @SuppressWarnings("unchecked")
    
    private static enum RelType implements RelationshipType
    {
       INCLUDES
    } 
   
    private static final String DB_PATH = "C:\\Users\\fereshteh\\Documents\\Neo4j\\MyDB3.graphdb";
    GraphDatabaseService  db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
 
    IndexManager nodeindex = db.index();
    Index<Node> names = nodeindex.forNodes( "node_auto_index" );
    IndexHits<Node> hits;
   
    ExecutionEngine ex = new ExecutionEngine(db,StringLogger.SYSTEM);
    ExecutionResult rst;
    
    Node [] nodes;
    Iterable<Relationship> rel;
    Transaction tx ;
    
    String [] str;
    String find;
    int t=0,s=0,h=0,c=0;
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        search_txb = new javax.swing.JTextField();
        search_btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText(" Enter The Word Please");

        search_btn.setText("Search");
        search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(search_btn)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search_txb, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(search_txb, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addComponent(search_btn)
                .addGap(50, 50, 50))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_btnActionPerformed
        
        
        RegisterShutdownHook( db );
        str=search_txb.getText().split(" ");
        
        if(db.index().existsForNodes("node_auto_index"))
        { 
            for(int i=0;i<str.length;++i)
            {
               find=str[i];
                 tx =db.beginTx();
                  try { 
                          hits = names.get("name", find);
                          for(int j=0;j<hits.size();++j) h++;
                          tx.success();
                      }            
                 
                  finally{tx.finish();} 
                 
            }
            nodes=new Node[h];
            for(int i=0;i<str.length;++i)
            {
               find=str[i];
                 tx =db.beginTx();
                  try { 
                          hits = names.get("name", find);
                       
                          if(hits != null)
                          {
                              while(hits.hasNext() && c<h)
                              { nodes [c] = hits.next();++c;++s;} 
                             
                          }
                         
                          tx.success();
                      }            
                 
                  finally{tx.finish();}   
            }
        }
       hits.close();   
      
        /* ------------------------------------------------------------*/
      
       for(int i=0;i<nodes.length;++i)
        {
             if(hits!=null)
             { 
               for(int j=i+1;j<nodes.length;++j)
               {  
                   if(hits!=null)
                   {
                    for( Relationship r:nodes[i].getRelationships())
                    {
                       
                      if(r!=null)
                      {
                        if(r.getOtherNode(nodes[i]).equals(nodes[j]))
                        {
                            tx =db.beginTx();
                            try { 
                                       r.setProperty("status", "on");
                                       ++t;
                                       tx.success();
                                }            
                 
                           finally{tx.finish();} 
                            
                        }
                      }
                    }
                   }
               }
             }
        }
        
        /*---------------------------------------------------------*/
        
        if(t>0 && s>0)
        {
          
           tx=db.beginTx();
           try {
                rst =  ex.execute
                 (
                  "START n=node(*) "+
                  "MATCH p=n-[rels:INCLUDES*]->m "+
                  "WHERE ALL (rel IN rels "+
                        "WHERE rel.status='on') "+
                  "WITH COLLECT(p) AS paths, MAX(length(p)) AS maxLength "+
                  "WITH FILTER(path IN paths WHERE length(path)= maxLength) AS longestPaths "+
                  "WITH EXTRACT(path IN longestPaths | LAST(path)) as last "+
                  "RETURN EXTRACT(x in last | x.link) AS Results; "
                 );
                tx.success();
           }
           finally{tx.finish();}
            
           JOptionPane.showMessageDialog(null,rst.dumpToString());
          
                   
        }
/*---------------------------------------------------------------------------*/        
        else if(t==0 && s>0)
        {
            for(int i=0;i<str.length;++i)
            {
              tx=db.beginTx();
              try {
                    rst =  ex.execute
                  (
                    "START n=node:node_auto_index(name='"+str[i]+"')"+ 
                 
                    "RETURN n.link AS Result; "
                  );
                    tx.success();
                  }
              finally{tx.finish();}
            JOptionPane.showMessageDialog(null,rst.dumpToString());
           }
        }
/*-----------------------------------------------------------------------*/        
        else if(s==0 && t==0) {JOptionPane.showMessageDialog(null," Doesn't Exist! ");}
        
        /*-------------------------------------------------------*/
        
       for(int i=0;i<nodes.length;++i)
         {
             if(nodes[i]!=null)
             { 
               for(int j=i+1;j<nodes.length;++j)
               {  
                   if(nodes[j]!=null)
                   {
                     for(Relationship r:nodes[i].getRelationships())
                    {
                       
                      if(r!=null)
                      {
                        if(r.getOtherNode(nodes[i]).equals(nodes[j]))
                         {
                            tx =db.beginTx();
                            try { 
                                       r.setProperty("status", "off");
                                       tx.success();
                                }            
                 
                           finally{tx.finish();} 
                      }
                    }
                   }
               }
              }
             }
         }
     //DO NOT FORGET IT---> 
       db.shutdown();
        System.exit(0);
    }//GEN-LAST:event_search_btnActionPerformed

    
    public static void main(String args[])
    {
       
        java.awt.EventQueue.invokeLater(new Runnable() {public void run() {new search_string().setVisible(true);} });
            
       
            
       
    }
    
    private static void RegisterShutdownHook( final GraphDatabaseService graphDb )
    {
       
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton search_btn;
    private javax.swing.JTextField search_txb;
    // End of variables declaration//GEN-END:variables
}
