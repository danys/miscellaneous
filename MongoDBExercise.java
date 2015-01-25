import java.net.UnknownHostException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
This class shows how to iterate through a collection in MongoDB 
and delete an element in an embedded array.
This class requires the MongoDB driver to work correctly.
*/

public class MongoDBExercise
{
	public static void main(String args[]) throws UnknownHostException
	{
		MongoClient client = new MongoClient();
		DB db = client.getDB("school");
		DBCollection coll = db.getCollection("students");
		DBCursor cur = coll.find();
		DBObject doc = null;
		int studentid=0, minindex;
		double minscore=0;
		double currentscore=0;
		BasicDBObject findo;
		while(cur.hasNext())
		{
			doc = cur.next();
			studentid = Integer.parseInt(doc.get("_id").toString());
			minscore=Double.MAX_VALUE;
			minindex=-1;
			BasicDBList list = (BasicDBList) doc.get("scores");
			String type;
			for(int j=0;j<list.size();j++)
			{
				BasicDBObject o = (BasicDBObject) list.get(j);
				type = o.getString("type").toString();
				currentscore = Double.parseDouble(o.get("score").toString());
				if ((currentscore<=minscore) && (type.compareTo("homework")==0))
				{
					minscore=currentscore;
					minindex = j;
				}
			}
			if (minindex!=-1)
			{
				list.remove(minindex);
				doc.removeField("scores");
				doc.put("scores", list);
			}
			findo = new BasicDBObject("_id",studentid);
			coll.update(findo, doc);
		}
	}
}