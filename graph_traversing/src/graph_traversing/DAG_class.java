
package graph_traversing;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;


public class DAG_class 
{
    
   
   
    private static final String DB_PATH = "C:\\Users\\fereshteh\\Documents\\Neo4j\\check";
    GraphDatabaseService  db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
 
    Node start,end;
    Object s,e;int s2,e2;
    
    Iterable<Relationship> rels=GlobalGraphOperations.at(db).getAllRelationships();
    Transaction tx ;
    
    
    public void check()
    {
        if(rels!=null)
        {
            for (Relationship rel: rels )
            {
                start=rel.getStartNode();end=rel.getEndNode();
                
                if(start!=null && end!=null)
                {
                    s=start.getProperty("priority");
                    e=end.getProperty("priority");
                    s2=(int)s;e2=(int)e;
                    if(s!=null && e!=null)
                    {
                       /* System.out.print(s);
                        System.out.print(e);
                        System.out.print(start);
                        System.out.print(end);
                        System.out.print(start.getId());
                        System.out.print(end.getId());*/
                        if(s2>e2 || start.getId()==end.getId())
                        {
                            tx =db.beginTx();
                            try {  rel.delete();tx.success();}
                            finally{tx.finish();}  
                           
                        }
                    }
                }
            }
        }
    }
}
