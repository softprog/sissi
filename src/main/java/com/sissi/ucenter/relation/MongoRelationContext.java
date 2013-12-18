package com.sissi.ucenter.relation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sissi.config.MongoConfig;
import com.sissi.context.JID;
import com.sissi.context.JID.JIDBuilder;
import com.sissi.protocol.iq.roster.Roster;
import com.sissi.ucenter.RelationContext;

/**
 * @author kim 2013-11-5
 */
public class MongoRelationContext implements RelationContext {

	private final static DBObject FILTER_MASTER = BasicDBObjectBuilder.start("master", 1).get();

	private final static DBObject FILTER_SLAVE = BasicDBObjectBuilder.start("slave", 1).get();

	private final static List<DBObject> QUERY_STATE;

	static {
		QUERY_STATE = new ArrayList<DBObject>();
		QUERY_STATE.add(BasicDBObjectBuilder.start().add("state", Roster.Subscription.TO.toString()).get());
		QUERY_STATE.add(BasicDBObjectBuilder.start().add("state", Roster.Subscription.BOTH.toString()).get());
	}

	private final Log log = LogFactory.getLog(this.getClass());

	private final MongoConfig config;

	private final JIDBuilder builder;

	public MongoRelationContext(MongoConfig config, JIDBuilder builder) {
		super();
		this.config = config;
		this.builder = builder;
	}

	private void updateEntityForQuery(JID from, JID to, DBObject entity) {
		DBObject query = BasicDBObjectBuilder.start().add("master", from.asStringWithBare()).add("slave", to.asStringWithBare()).get();
		this.log.debug("Query is: " + query);
		this.log.debug("Entity is: " + entity);
		this.config.collection().update(query, entity);
	}

	@Override
	public RelationContext establish(JID from, Relation relation) {
		DBObject query = BasicDBObjectBuilder.start().add("master", from.asStringWithBare()).add("slave", this.builder.build(relation.getJID()).asStringWithBare()).get();
		DBObject entity = BasicDBObjectBuilder.start("$set", BasicDBObjectBuilder.start(relation.plus()).add("name", relation.getName()).add("state", relation.getSubscription()).get()).get();
		this.log.debug("Query is: " + query);
		this.log.debug("Entity is: " + entity);
		this.config.collection().update(query, entity, true, true);
		return this;
	}

	@Override
	public RelationContext update(JID from, JID to, String state) {
		DBObject entity = BasicDBObjectBuilder.start().add("$set", BasicDBObjectBuilder.start().add("state", state).get()).get();
		this.updateEntityForQuery(from, to, entity);
		return this;
	}

	public RelationContext remove(JID from, JID to) {
		DBObject query = BasicDBObjectBuilder.start().add("master", from.asStringWithBare()).add("slave", to.asStringWithBare()).get();
		this.log.debug("Query is: " + query);
		this.config.collection().remove(query);
		return this;
	}

	@Override
	public Set<Relation> myRelations(JID from) {
		DBObject query = BasicDBObjectBuilder.start().add("master", from.asStringWithBare()).get();
		this.log.debug("Query is: " + query);
		return new MongoRelations(this.config.collection().find(query), this.config);
	}

	@Override
	public Relation ourRelation(JID from, JID to) {
		DBObject query = BasicDBObjectBuilder.start().add("master", from.asStringWithBare()).add("slave", to.asStringWithBare()).get();
		this.log.debug("Query is: " + query);
		DBObject db = this.config.collection().findOne(query);
		return db != null ? new MongoRelation(db, this.config) : null;
	}

	@Override
	public Set<String> whoSubscribedMe(JID from) {
		DBObject query = BasicDBObjectBuilder.start().add("slave", from.asStringWithBare()).add("$or", QUERY_STATE).get();
		this.log.debug("Query is: " + query);
		return new JIDs(this.config.collection().find(query, FILTER_MASTER), "master");
	}

	public Set<String> iSubscribedWho(JID from) {
		DBObject query = BasicDBObjectBuilder.start("master", from.asStringWithBare()).add("$or", QUERY_STATE).get();
		this.log.debug("Query is: " + query);
		return new JIDs(this.config.collection().find(query, FILTER_SLAVE), "slave");
	}

	private class JIDs extends HashSet<String> {

		private static final long serialVersionUID = 1L;

		public JIDs(DBCursor cursor, String key) {
			if (cursor == null) {
				return;
			}
			while (cursor.hasNext()) {
				this.add(cursor.next().get(key).toString());
			}
		}
	}

	public class MongoRelations extends HashSet<Relation> {

		private static final long serialVersionUID = 1L;

		public MongoRelations(DBCursor cursor, MongoConfig config) {
			if (cursor == null) {
				return;
			}
			while (cursor.hasNext()) {
				this.add(new MongoRelation(cursor.next(), config));
			}
		}
	}
}