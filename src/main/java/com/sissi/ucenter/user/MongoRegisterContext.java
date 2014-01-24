package com.sissi.ucenter.user;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.MongoException;
import com.sissi.config.MongoConfig;
import com.sissi.context.JIDBuilder;
import com.sissi.protocol.iq.data.XField;
import com.sissi.protocol.iq.data.XValue;
import com.sissi.protocol.iq.register.simple.Username;
import com.sissi.ucenter.RegisterContext;
import com.sissi.ucenter.field.Field;
import com.sissi.ucenter.field.Fields;

/**
 * @author kim 2013年12月3日
 */
public class MongoRegisterContext extends MongoFieldContext implements RegisterContext {

	private final MongoConfig config;

	private final JIDBuilder jidBuilder;

	public MongoRegisterContext(MongoConfig config, JIDBuilder jidBuilder) {
		super();
		this.config = config;
		this.jidBuilder = jidBuilder;
	}

	@Override
	public Boolean register(Fields fields) {
		try {
			return this.valid(fields) ? (this.config.collection().save(super.getEntities(fields, BasicDBObjectBuilder.start())).getError() == null) : false;
		} catch (MongoException e) {
			return false;
		}
	}

	private Boolean valid(Fields fields) {
		for (Field<?> field : fields.findField(Username.NAME, XField.class).getChildren()) {
			if (field.getClass() == XValue.class) {
				String username = String.class.cast(field.getValue());
				return !username.isEmpty() && this.jidBuilder.build(username, null).isValid(false);
			}
		}
		return false;
	}
}
