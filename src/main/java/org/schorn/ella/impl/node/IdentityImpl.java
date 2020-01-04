/* 
 * The MIT License
 *
 * Copyright 2019 Bryan Schorn.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.schorn.ella.impl.node;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.ActiveNode.Identity;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class IdentityImpl extends AbstractContextual implements Identity {

    static final Logger LGR = LoggerFactory.getLogger(IdentityImpl.class);

    /**
     * Fields of Identity ObjectData
     */
    public enum Fields implements ValueTypeMember {
        identity_type__idata_idtype,
        identity_type__idata_user,
        identity_type__idata_id,
        identity_type__idata_uuid,
        identity_type__idata_cts,;
        private ActiveNode.ValueTypeMember valueTypeMember;

        Fields() {
            String[] obj_val = this.name().split("__");
            if (obj_val.length == 2) {
                String object_type = obj_val[0];
                String value_type = obj_val[1];
                ObjectType objectType = ObjectType.get(AppContext.Common, object_type);
                ValueType valueType = ValueType.get(AppContext.Common, value_type);
                try {
                    this.valueTypeMember = ActiveNode.ValueTypeMember.get(objectType, valueType);
                } catch (Exception e) {
                    LGR.error("{}.ctor() - %s Caught Exception: {]",
                            Fields.class.getSimpleName(),
                            this.name(),
                            Functions.getStackTraceAsString(e));

                }
            }
        }

        public ActiveNode.ValueTypeMember valueTypeMember() {
            return this.valueTypeMember;
        }

        @Override
        public ObjectType memberOfType() {
            return this.valueTypeMember.memberOfType();
        }

        @Override
        public int index() {
            return this.valueTypeMember.index();
        }

        @Override
        public ValueType memberType() {
            return this.valueTypeMember.memberType();
        }

        @Override
        public String asValueTypeMemberStr() {
            return this.valueTypeMember.asValueTypeMemberStr();
        }
    }

    /*
	 * 
     */
    static private AtomicInteger LASTID = new AtomicInteger(0);

    /**
     *
     * @param context
     * @param identityType
     * @param userId
     * @return
     * @throws Exception
     */
    public static Identity create(AppContext context, IdentityType identityType, String userId) throws Exception {
        ObjectData.Builder builder = Fields.identity_type__idata_user.valueTypeMember.memberOfType().builder();
        builder.add(Fields.identity_type__idata_idtype.memberType().create(identityType.name()));
        builder.add(Fields.identity_type__idata_user.memberType().create(userId));
        builder.add(Fields.identity_type__idata_id.memberType().create(LASTID.incrementAndGet()));
        builder.add(Fields.identity_type__idata_uuid.memberType().create(UUID.randomUUID()));
        builder.add(Fields.identity_type__idata_cts.memberType().create(LocalDateTime.now()));
        return new IdentityImpl(context, builder.build());
    }

    /*
	 * 
     */
    private ObjectData identityObj;

    /*
	 * 
     */
    public IdentityImpl(AppContext context, ObjectData identityObj) {
        super(context);
        this.identityObj = identityObj;
    }

    @Override
    public IdentityType type() {
        return IdentityType.valueOf((String) this.identityObj.get(Fields.identity_type__idata_idtype.index()).activeValue().toString());
    }

    @Override
    public String userId() {
        return (String) this.identityObj.get(Fields.identity_type__idata_user.index()).activeValue().toString();
    }

    @Override
    public Integer identityId() {
        return (Integer) this.identityObj.get(Fields.identity_type__idata_id.index()).activeValue();
    }

    @Override
    public String uniqueId() {
        return this.identityObj.get(Fields.identity_type__idata_uuid.index()).activeValue().toString();
    }

    @Override
    public UUID uuid() {
        return UUID.fromString(this.identityObj.get(Fields.identity_type__idata_uuid.index()).activeValue().toString());
    }

    @Override
    public LocalDateTime createTS() {
        return (LocalDateTime) this.identityObj.get(Fields.identity_type__idata_cts.index()).activeValue();
    }

    @Override
    public String name() {
        return String.format("%s.%s", this.context().name(), this.userId());
    }

    @Override
    public String toString() {
        return String.format("%s.%s [%d] (%s)",
                this.context().name(),
                this.userId(),
                this.identityId(),
                this.type().name());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Identity) {
            return this.compareTo((Identity) object) == 0;
        }
        return false;
    }

    @Override
    public int compareTo(Identity that) {
        return this.uuid().compareTo(that.uuid());
    }

}
