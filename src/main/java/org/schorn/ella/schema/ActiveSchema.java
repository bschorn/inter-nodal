/*
 * The MIT License
 *
 * Copyright 2019 bschorn.
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
package org.schorn.ella.schema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.schorn.ella.convert.TypeConverter;
import org.schorn.ella.node.BondType;
import org.schorn.ella.node.DataGroup;
import org.schorn.ella.node.TypeAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bschorn
 */
public class ActiveSchema {
    static Formatter<ActiveSchema, String> FORMATTER = new ActiveSchemaFormats.Reformat();
    static Formatter<ActiveSchema, Map> METAMAPPER = new ActiveSchemaFormats.Meta();

    static private final String RESOURCES_PATH = "D:/Users/bschorn/documents/GitHub/jane-bank/src/main/resources";

    static public <T> T typeConversion(Class<T> classOfT, String value) {
        try {
            return (T) TypeConverter.recast(String.class, classOfT, value);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    static public List<String> readFile(String path, String file) {
        String fileName = String.format("%s/%s",
                path == null ? RESOURCES_PATH : path,
                file);
        List<String> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            list = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public enum AttributeType {
        ObjectCategory("object_category"),
        ObjectPurpose("object_purpose"),
        ObjectLevel("object_level");

        private final String attributeName;

        AttributeType(String attributeName) {
            this.attributeName = attributeName;
        }

        public String attributeName() {
            return this.attributeName;
        }
    }

    /**
     *
     */
    public enum Roles implements Predicate<Type> {
        BondType('`', Type.class, false, null, null),
        DataType('*', Type.class, false, null, null),
        FieldType('^', FieldType.class, true, new FieldTypeFormats.Reformat(), new FieldTypeFormats.Meta()),
        ValueType('$', ValueType.class, true, new ValueTypeFormats.Reformat(), new ValueTypeFormats.Meta()),
        Fragment('~', Fragment.class, true, new FragmentFormats.Reformat(), null),
        BaseType('%', BaseType.class, true, new BaseTypeFormats.Reformat(), null),
        ObjectType('#', ObjectType.class, true, new ObjectTypeFormats.Reformat(), new ObjectTypeFormats.Meta()),
        ArrayType('@', ArrayType.class, false, null, null),
        Constraints('!', Constraints.class, false, null, null),
        Member('%', Member.class, false, new MemberFormats.Reformat(), new MemberFormats.Meta()),
        Parent('-', Parent.class, false, null, null),
        Attribute('¯', Type.class, false, null, null),
        Unset('x', Type.class, false, null, null);

        private final Character operator;
        private final Class<? extends Type> valueClass;
        private final boolean display;
        private final TypeFormatter<? extends Type, String> reformatter;
        private final TypeFormatter<? extends Type, Map> metamap;

        Roles(Character operator,
                Class<? extends Type> valueClass,
                boolean display,
                TypeFormatter<? extends Type, String> reformatter,
                TypeFormatter<? extends Type, Map> metamap) {
            this.operator = operator;
            this.valueClass = valueClass;
            this.display = display;
            this.reformatter = reformatter;
            this.metamap = metamap;
        }

        public Character symbol() {
            return this.operator;
        }

        public boolean display() {
            return this.display;
        }

        @Override
        public boolean test(Type type) {
            return this.equals(type.role());
        }

        public TypeFormatter<?, String> reformatter() {
            return this.reformatter;
        }
        public TypeFormatter<?, Map> metamap() {
            return this.metamap;
        }

        static public Roles roleForOperator(Character symbol) {
            for (Roles types : Roles.values()) {
                if (Objects.equals(types.operator, symbol)) {
                    return types;
                }
            }
            return null;
        }
        static public Roles roleForClass(Class<?> valueClass) {
            for (Roles types : Roles.values()) {
                if (valueClass.equals(types.valueClass)) {
                    return types;
                }
            }
            return null;
        }

        static public Roles parse(String typeName) {
            for (Roles types : Roles.values()) {
                if (types.name().equalsIgnoreCase(typeName)
                        || types.name().equalsIgnoreCase(typeName.substring(0, typeName.length() - 1))
                        || types.symbol().equals(typeName.charAt(0))) {
                    return types;
                }
            }
            return null;
        }
    }

    /**
     *
     */
    interface Type {

        Roles role();

        String name();

        boolean isResolved();

        /**
         * dependency resolver
         */
        void resolve() throws Exception;

        Map metaMap();
    }

    interface LeafType extends Type, Comparable<LeafType> {

        @Override
        default int compareTo(LeafType that) {
            return this.name().compareTo(that.name());
        }
    }

    interface BranchType extends Type {
        List<Member> members();
    }

    static public class Constraints implements Type {

        private boolean resolved = false;
        private Map<String, Object> map = new HashMap<>();

        public void addConstraint(String name, Object values) {
            this.map.put(name, values);
        }

        public int count() {
            return this.map.size();
        }

        @Override
        public String name() {
            return "Constraints";
        }

        @Override
        public Roles role() {
            return Roles.Constraints;
        }

        @Override
        public boolean isResolved() {
            return this.resolved;
        }

        @Override
        public void resolve() throws Exception {
            this.resolved = true;
        }

        @Override
        public Map metaMap() {
            return this.map;
        }

        @Override
        public String toString() {
            /*
            StringJoiner joiner = new StringJoiner(", ", "{ ", " }");
            for (String key : this.names) {
                Object object = this.values.get(key);
                if (object != null) {
                    if (object instanceof Number) {
                        joiner.add(String.format("%s: %s", key, object.toString()));
                    } else if (object instanceof List) {
                        StringJoiner aryjoiner = new StringJoiner("\",\"", "[\"", "\"]");
                        List list = (List) object;
                        list.stream().forEach(i -> aryjoiner.add(i.toString()));
                        joiner.add(String.format("%s: %s", key, aryjoiner.toString()));
                    } else {
                        joiner.add(String.format("%s: \"%s\"", key, object.toString()));
                    }
                }
            }
            return joiner.toString();
             */
            return null;
        }

    }

    static public class FieldType implements LeafType {

        static TypeFormatter<FieldType, String> FORMATTER = new FieldTypeFormats.FormatOne();

        @Override
        public Roles role() {
            return Roles.FieldType;
        }

        private final ActiveSchema schema;
        private final String name;
        private String dataTypeName;
        private DataGroup dataGroup = DataGroup.TEXT;
        private Constraints constraints = null;

        public FieldType(ActiveSchema schema, String name) {
            this.schema = schema;
            this.name = name;
            this.dataTypeName = "TEXT";
        }
        @Override
        public String name() {
            return this.name;
        }

        public String dataTypeName() {
            return this.dataTypeName;
        }

        public void setDataTypeName(String dataTypeName) {
            this.dataGroup = DataGroup.valueOf(dataTypeName);
            this.dataTypeName = dataTypeName;
        }

        public DataGroup getDataGroup() {
            return DataGroup.valueOf(this.dataTypeName);
        }

        public void setConstraints(Constraints constraints) {
            this.constraints = constraints;
        }

        public Constraints constraints() {
            return this.constraints;
        }

        @Override
        public String toString() {
            return FORMATTER.apply(this);
        }

        private boolean resolved = false;

        @Override
        public boolean isResolved() {
            return this.resolved;
        }

        @Override
        public void resolve() throws Exception {
            if (this.dataGroup == null) {
                throw new Exception(String.format(
                        "Unresolved DataType '%s' referenced in FieldType: '%s'",
                        this.dataTypeName,
                        this.name));
            }
            this.resolved = true;
        }

        @Override
        public Map metaMap() {
            return ((FieldTypeFormats.Meta) this.role().metamap()).apply(this);
        }

    }

    static public class ValueType implements LeafType {

        static TypeFormatter<ValueType, String> FORMATTER = new ValueTypeFormats.Reformat();

        @Override
        public Roles role() {
            return Roles.ValueType;
        }

        private final ActiveSchema schema;
        private final String name;
        private String fieldTypeName = "text_type";

        ValueType(ActiveSchema schema, String name) {
            this.schema = schema;
            this.name = name;
        }
        @Override
        public String name() {
            return this.name;
        }

        public String fieldTypeName() {
            return this.fieldTypeName;
        }

        public List<String> members() {
            return Arrays.asList(new String[]{this.fieldTypeName});
        }

        public void setFieldTypeName(String fieldTypeName) {
            this.fieldTypeName = fieldTypeName;
        }

        @Override
        public String toString() {
            return FORMATTER.apply(this);
        }

        private boolean resolved = false;

        @Override
        public boolean isResolved() {
            return this.resolved;
        }

        @Override
        public void resolve() throws Exception {
            FieldType fieldType = this.schema.getFieldType(this.fieldTypeName);
            this.resolved = (fieldType != null && fieldType.isResolved());
        }
        @Override
        public Map metaMap() {
            return ((ValueTypeFormats.Meta) this.role().metamap()).apply(this);
        }

    }

    static public class Fragment extends ObjectType {

        @Override
        public Roles role() {
            return Roles.Fragment;
        }

        Fragment(ActiveSchema schema, String name) {
            super(schema, name);
        }

    }

    static public class BaseType extends ObjectType {

        static TypeFormatter<ObjectType, String> FORMATTER = new ObjectTypeFormats.FormatOne();

        @Override
        public Roles role() {
            return Roles.BaseType;
        }

        BaseType(ActiveSchema schema, String name) {
            super(schema, name);
        }

        @Override
        public String toString() {
            return FORMATTER.apply(this);
        }
    }

    static public class ObjectType implements BranchType, Comparable<ObjectType> {

        static TypeFormatter<ObjectType, String> FORMATTER = new ObjectTypeFormats.FormatOne();

        static public final Map<String, Integer> TYPE_NAMES_ORD = new HashMap<>();

        private boolean resolved = false;

        private final ActiveSchema schema;
        private final String name;
        private final List<Parent> parents = new ArrayList<>();
        private final List<Member> members = new ArrayList<>();
        private final Map<AttributeType, String> attributes = new HashMap<>();

        ObjectType(ActiveSchema schema, String name) {
            this.schema = schema;
            this.name = name;
            TYPE_NAMES_ORD.put(this.qualifiedName(), TYPE_NAMES_ORD.size());
        }

        @Override
        public Roles role() {
            return Roles.ObjectType;
        }

        @Override
        public String name() {
            return this.name;
        }

        final public String qualifiedName() {
            return String.format("%s.%s",
                    this.getClass().getSimpleName(), this.name);
        }

        @Override
        public List<Member> members() {
            return this.members;
        }

        public ActiveSchema schema() {
            return this.schema;
        }
        public void addParent(Parent parentType) {
            this.parents.add(parentType);
        }
        public void addMember(Member member) {
            this.members.add(member);
        }

        public void addAttribute(AttributeType attributeType, String attributeValue) {
            this.attributes.put(attributeType, attributeValue);
        }

        public List<Parent> parents() {
            return this.parents;
        }

        public Map<AttributeType, String> attributes() {
            return this.attributes;
        }

        @Override
        public String toString() {
            return FORMATTER.apply(this);
        }

        @Override
        public boolean isResolved() {
            return this.resolved;
        }

        @Override
        public void resolve() throws Exception {
            for (Map.Entry<AttributeType, String> entry : this.attributes.entrySet()) {
                Object attrValue = null;
                switch (entry.getKey()) {
                    case ObjectCategory:
                        attrValue = TypeAttributes.ObjectCategory.valueOf(entry.getValue());
                        break;
                    case ObjectPurpose:
                        attrValue = TypeAttributes.ObjectPurpose.valueOf(entry.getValue());
                        break;
                    case ObjectLevel:
                        attrValue = TypeAttributes.ObjectLevel.valueOf(entry.getValue());
                        break;
                }
                if (attrValue == null) {
                    throw new Exception(String.format("Unresolved Attribute '%s.%s' in ObjectType.%s:\n\t",
                            entry.getKey().getClass().getSimpleName(),
                            entry.getValue(),
                            this.name));
                }
            }
            List<Member> importedMembers = new ArrayList<>();
            for (Parent parentType : this.parents) {
                Type type = this.schema.getType(parentType.name());
                switch (type.role()) {
                    case BaseType:
                    case ObjectType:
                        ObjectType otype = (ObjectType) type;
                        otype.members().forEach((m) -> {
                            Member member = new Member(m.schema, this.role(), this.name, m);
                            if (importedMembers.stream().filter(me -> me.hasConflict(member)).findAny().isEmpty()) {
                                if (!importedMembers.contains(member)) {
                                    importedMembers.add(member);
                                }
                            }
                        });
                        break;

                }
            }
            for (Member importedMember : importedMembers) {
                importedMember.resolve();
            }
            this.members.addAll(0, importedMembers);
            List<Member> resolvedMembers = new ArrayList<>();
            List<Member> unresolvedMembers = new ArrayList<>();
            for (Member member : this.members) {
                Type type = this.schema.getType(member.name());
                if (type != null && type.isResolved()) {
                    if (member.memberRole == Roles.Fragment) {
                        Fragment fragment = (Fragment) type;
                        for (Member tmember : fragment.members()) {
                            if (resolvedMembers.stream()
                                    .filter(m -> m.name().equals(tmember.name()))
                                    .findAny()
                                    .isEmpty()) {
                                resolvedMembers.add(tmember);
                            }
                        }
                    } else {
                        resolvedMembers.add(member);
                    }
                } else {
                    unresolvedMembers.add(member);
                }
            }
            if (!unresolvedMembers.isEmpty()) {
                String header = String.format("Unresolved members ObjectType.%s:\n\t", this.name);
                StringJoiner joiner = new StringJoiner("\n\t", header, "");
                for (Member member : unresolvedMembers) {
                    joiner.add(member.toString());
                }
                throw new Exception(joiner.toString());
            }
            this.members.clear();
            this.members.addAll(resolvedMembers);
            this.resolved = true;
        }

        @Override
        public int compareTo(ObjectType other) {
            ObjectType that = (ObjectType) other;
            Integer a = TYPE_NAMES_ORD.get(this.qualifiedName());
            Integer b = TYPE_NAMES_ORD.get(that.qualifiedName());
            if (a != null && b != null) {
                return Integer.compare(a, b);
            }
            return -1;
        }

        @Override
        public Map metaMap() {
            return ((ObjectTypeFormats.Meta) this.role().metamap()).apply(this);
        }
    }

    static public class Parent implements Type {

        @Override
        public Roles role() {
            return Roles.Parent;
        }
        private boolean resolved = true;
        private final ActiveSchema schema;
        private final Roles parentRole;
        private final String parentName;

        public Parent(ActiveSchema schema, Roles parentRole, String parentName) {
            this.schema = schema;
            this.parentRole = parentRole;
            this.parentName = parentName;
        }

        @Override
        public String name() {
            return String.format("%s.%s", this.parentRole.name(), this.parentName);
        }

        public Roles parentRole() {
            return this.parentRole;
        }

        public String parentName() {
            return this.parentName;
        }

        @Override
        public String toString() {
            return String.format("%s.%s", this.parentRole.name(), this.parentName);
        }

        @Override
        public boolean isResolved() {
            return this.resolved;
        }

        @Override
        public void resolve() throws Exception {
            Type type = null;
            switch (this.parentRole) {
                case ValueType:
                    type = this.schema.getValueType(parentName);
                    break;
                case Fragment:
                    type = this.schema.getFragment(parentName);
                    break;
                case ObjectType:
                    type = this.schema.getObjectType(parentName);
                    break;
                case ArrayType:
                    type = this.schema.getArrayType(parentName);
                    break;
                default:
                    break;
            }
            this.resolved = (type != null);
        }

        @Override
        public Map metaMap() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    static public class Member implements Type, Comparable<Member> {

        static TypeFormatter<Member, String> FORMATTER = new MemberFormats.FormatOne();

        static final Logger LGR = LoggerFactory.getLogger(Member.class);
        static private final Map<String, String> KEYS_TO_OWNERS = new HashMap<>();

        @Override
        public Roles role() {
            return Roles.Member;
        }
        private final ActiveSchema schema;
        // Member Definition
        private final Roles ownerRole;
        private final String ownerName;
        private final Roles memberRole;
        private final String memberName;
        // Member Attributes
        private BondType bondType;
        private String foreignKeyObjectTypeName = null;
        private String foreignKeyValueTypeName = null;
        private Member inheritedMember = null;

        public Member(ActiveSchema schema,
                Roles ownerRole,
                String ownerName,
                Roles memberRole,
                String memberName) {
            this.ownerRole = ownerRole;
            this.ownerName = ownerName;
            this.schema = schema;
            this.memberRole = memberRole;
            this.memberName = memberName;
            this.bondType = BondType.IMMUTABLE;
            this.inheritedMember = this;
        }

        public Member(ActiveSchema schema,
                Roles ownerRole,
                String ownerName,
                Member inheritedMember) {
            this.ownerRole = ownerRole;
            this.ownerName = ownerName;
            this.schema = schema;
            this.memberRole = inheritedMember.memberRole;
            this.memberName = inheritedMember.memberName;
            this.setBondType(inheritedMember.bondType.name());
            this.inheritedMember = inheritedMember;
        }

        @Override
        public String name() {
            return String.format("%s.%s", this.memberRole.name(), this.memberName);
        }

        public String ownership() {
            return String.format("%s.%s", this.ownerRole.name(), this.ownerName);
        }

        public String shortName() {
            return String.format("%s%s", this.memberRole.symbol(), this.memberName);
        }

        public String memberName() {
            return this.memberName;
        }
        public Roles memberRole() {
            return this.memberRole;
        }

        public String fullMemberName() {
            return String.format("%s.%s", this.ownerName, this.memberName);
        }

        public Roles ownerRole() {
            return this.ownerRole;
        }

        public String ownerName() {
            return this.ownerName;
        }

        public boolean isInherited() {
            return this.inheritedMember != null && this.inheritedMember != this;
        }

        public Roles originalOwnerRole() {
            return this.inheritedMember.ownerRole;
        }

        public String originalOwnerName() {
            return this.inheritedMember.ownerName;
        }

        public String originalOwnership() {
            return this.inheritedMember.ownership();
        }

        public void setBondType(String bondType) {
            if (bondType != null) {
                BondType bondType0 = BondType.valueOf(bondType);
                if (bondType0 != null) {
                    this.bondType = bondType0;
                    if (BondType.KEYS.contains(this.bondType)) {
                        if (!KEYS_TO_OWNERS.containsKey(this.name())) {
                            KEYS_TO_OWNERS.put(this.name(), this.ownership());
                        }
                    }
                }
            }

        }

        public BondType getBondType() {
            return this.bondType;
        }

        public void addForeignKey(String objectTypeName, String valueTypeName) {
            this.foreignKeyObjectTypeName = objectTypeName;
            this.foreignKeyValueTypeName = valueTypeName;
        }

        public String getForeignKeyObjectTypeName() {
            return this.foreignKeyObjectTypeName;
        }

        public String getForeignKeyValueTypeName() {
            return this.foreignKeyValueTypeName;
        }

        @Override
        public String toString() {
            return FORMATTER.apply(this);
        }

        private boolean resolved = false;

        @Override
        public boolean isResolved() {
            return this.resolved;
        }

        @Override
        public void resolve() throws Exception {
            Type type = null;
            switch (this.memberRole) {
                case ValueType:
                    type = this.schema.getValueType(this.memberName);
                    break;
                case Fragment:
                    type = this.schema.getFragment(this.memberName);
                    break;
                case ObjectType:
                    type = this.schema.getObjectType(this.memberName);
                    break;
                case ArrayType:
                    type = this.schema.getArrayType(this.memberName);
                    break;
                default:
                    break;
            }
            if (type == null || !type.isResolved()) {
                throw new Exception(String.format("Unresolved MemberType '%s'. Suggestion:\n%ss.def(\"%s\", FieldType.text_type)\n",
                        this.memberName, this.memberRole.name(), this.memberName));
            }
            if (this.bondType.equals(BondType.FOREIGN_KEY)) {
                if (this.foreignKeyValueTypeName == null) {
                    String ownership = KEYS_TO_OWNERS.get(this.name());
                    if (ownership != null) {
                        this.foreignKeyValueTypeName = this.memberName;
                        this.foreignKeyObjectTypeName = ownership.split("\\.")[1];
                    }
                }
            }
            if (this.foreignKeyObjectTypeName != null) {
                type = this.schema.getObjectType(this.foreignKeyObjectTypeName);
                if (!type.isResolved()) {
                    throw new Exception(String.format(
                            "Unresolved FK ObjectType '%s'", this.foreignKeyObjectTypeName));
                }
            }
            if (this.foreignKeyValueTypeName != null) {
                type = this.schema.getValueType(this.foreignKeyValueTypeName);
                if (!type.isResolved()) {
                    throw new Exception(String.format(
                            "Unresolved FK ValueType '%s'", this.foreignKeyValueTypeName));
                }
            }
            if (this.bondType.equals(BondType.FOREIGN_KEY)) {
                if (this.foreignKeyValueTypeName == null || this.foreignKeyObjectTypeName == null) {
                    throw new Exception("Why can't I find the FK owner? " + this.name());
                }
            }
            this.resolved = true;
        }

        public boolean hasConflict(Member that) {
            if (this.memberName.compareTo(that.memberName) == 0) {
                if (this.memberRole.compareTo(that.memberRole) == 0) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int compareTo(Member that) {
            int a = this.memberName.compareTo(that.memberName);
            if (a != 0) {
                return a;
            }
            int b = this.memberRole.compareTo(that.memberRole);
            if (b != 0) {
                return b;
            }
            int c = this.ownerName.compareTo(that.ownerName);
            if (c != 0) {
                return c;
            }
            int d = this.ownerRole.compareTo(that.ownerRole);
            if (d != 0) {
                return d;
            }
            int e = this.inheritedMember.compareTo(that.inheritedMember);
            if (e != 0) {
                return e;
            }
            int f = this.bondType.compareTo(that.bondType);
            if (f != 0) {
                return f;
            }
            int g = this.foreignKeyObjectTypeName.compareTo(that.foreignKeyObjectTypeName);
            if (g != 0) {
                return g;
            }
            int h = this.foreignKeyValueTypeName.compareTo(that.foreignKeyValueTypeName);
            return h;
        }

        @Override
        public Map metaMap() {
            return ((MemberFormats.Meta) this.role().metamap()).apply(this);
        }
    }

    static public class ArrayType implements BranchType {

        @Override
        public Roles role() {
            return Roles.ArrayType;
        }

        ArrayType(ActiveSchema schema, String name) {
            this.schema = schema;
            this.name = name;
        }

        @Override
        public String name() {
            return this.name;
        }

        public void setMemberType(Member memberType) {
            this.memberType = memberType;
        }

        @Override
        public List<Member> members() {
            return Arrays.asList(new Member[]{this.memberType});
        }


        private final ActiveSchema schema;
        private final String name;
        private Member memberType;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            return builder.toString();
        }

        private boolean resolved = false;

        @Override
        public boolean isResolved() {
            return this.resolved;
        }

        @Override
        public void resolve() throws Exception {
            this.resolved = this.memberType.isResolved();
        }

        @Override
        public Map metaMap() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    private String context = "unknown";

    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return this.context;
    }

    private final Set<BranchType> branchTypes = new TreeSet<>();

    private final Set<LeafType> leafTypes = new TreeSet<>();

    public <T> T create(Class<T> classOfT, String name) throws Exception {
        Roles role = Roles.roleForClass(classOfT);
        switch (role) {
            case FieldType:
                return (T) createFieldType(name);
            case ValueType:
                return (T) createValueType(name);
            case Fragment:
                return (T) createFragment(name);
            case BaseType:
                return (T) createBaseType(name);
            case ObjectType:
                return (T) createObjectType(name);
            case ArrayType:
                return (T) createArrayType(name);
        }
        return null;
    }

    public Type getType(String qualifiedName) {
        Roles role = Roles.Unset;
        String name = null;
        String[] parts = qualifiedName.split("\\.");
        if (parts.length == 2) {
            role = Roles.parse(parts[0]);
            name = parts[1];
        } else {
            role = Roles.roleForOperator(qualifiedName.charAt(0));
            name = qualifiedName.substring(1, qualifiedName.length());
        }
        switch (role) {
            case FieldType:
                return getFieldType(name);
            case ValueType:
                return getValueType(name);
            case Fragment:
                return getFragment(name);
            case BaseType:
                return getBaseType(name);
            case ObjectType:
                return getObjectType(name);
            case ArrayType:
                return getArrayType(name);
        }
        return null;
    }


    private ArrayType createArrayType(String name) {
        ArrayType arrayType = getArrayType(name);
        if (arrayType == null) {
            arrayType = new ArrayType(this, name);
            this.branchTypes.add(arrayType);
        }
        return arrayType;
    }

    private ArrayType getArrayType(String name) {
        Optional<ArrayType> optArrayType
                = this.branchTypes.stream()
                        .filter(t -> t.name().equals(name))
                        .filter(t -> t.role().equals(Roles.ArrayType))
                        .map(t -> ArrayType.class.cast(t))
                        .findAny();
        if (optArrayType.isPresent()) {
            return optArrayType.get();
        }
        return null;
    }

    private ObjectType createObjectType(String name) throws Exception {
        ObjectType objectType = getObjectType(name);
        if (objectType == null) {
            objectType = new ObjectType(this, name);
            this.branchTypes.add(objectType);
        } else {
            throw new Exception(String.format("%s.%s has already been defined.",
                    ObjectType.class.getSimpleName(), name));
        }
        return objectType;
    }

    private ObjectType getObjectType(String name) {
        Optional<ObjectType> optObjectType
                = this.branchTypes.stream()
                        .filter(t -> t.name().equals(name))
                        .filter(t -> t.role().equals(Roles.ObjectType))
                        .map(t -> ObjectType.class.cast(t))
                        .findAny();
        if (optObjectType.isPresent()) {
            return optObjectType.get();
        }
        return null;
    }

    private Fragment createFragment(String name) {
        Fragment template = getFragment(name);
        if (template == null) {
            template = new Fragment(this, name);
            this.branchTypes.add(template);
        }
        return template;
    }

    private Fragment getFragment(String name) {
        Optional<Fragment> optFragment
                = this.branchTypes.stream()
                        .filter(t -> t.name().equals(name))
                        .filter(t -> t.role().equals(Roles.Fragment))
                        .map(t -> Fragment.class.cast(t))
                        .findAny();
        if (optFragment.isPresent()) {
            return optFragment.get();
        }
        return null;
    }

    private BaseType createBaseType(String name) {
        BaseType abstractType = getBaseType(name);
        if (abstractType == null) {
            abstractType = new BaseType(this, name);
            this.branchTypes.add(abstractType);
        }
        return abstractType;
    }

    private BaseType getBaseType(String name) {
        Optional<BaseType> optAbstractType
                = this.branchTypes.stream()
                        .filter(t -> t.name().equals(name))
                        .filter(t -> t.role().equals(Roles.BaseType))
                        .map(t -> BaseType.class.cast(t))
                        .findAny();
        if (optAbstractType.isPresent()) {
            return optAbstractType.get();
        }
        return null;
    }

    private ValueType createValueType(String name) {
        ValueType valueType = getValueType(name);
        if (valueType == null) {
            valueType = new ValueType(this, name);
            this.leafTypes.add(valueType);
        }
        return valueType;
    }

    private ValueType getValueType(String name) {
        Optional<ValueType> optValueType
                = this.leafTypes.stream()
                        .filter(t -> t.name().equals(name))
                        .filter(t -> t.role().equals(Roles.ValueType))
                        .map(t -> ValueType.class.cast(t))
                        .findAny();
        if (optValueType.isPresent()) {
            return optValueType.get();
        }
        return null;
    }

    private FieldType createFieldType(String name) {
        FieldType fieldType = getFieldType(name);
        if (fieldType == null) {
            fieldType = new FieldType(this, name);
            this.leafTypes.add(fieldType);
        }
        return fieldType;
    }

    private FieldType getFieldType(String name) {
        Optional<FieldType> optFieldType
                = this.leafTypes.stream()
                        .filter(ot -> ot.name().equals(name))
                        .filter(t -> t.role().equals(Roles.FieldType))
                        .map(t -> FieldType.class.cast(t))
                        .findAny();
        if (optFieldType.isPresent()) {
            return optFieldType.get();
        }
        return null;
    }

    /**
     *
     * @param type
     */
    public void add(Type type) {
        if (type instanceof LeafType) {
            this.leafTypes.add((LeafType) type);
        } else if (type instanceof BranchType) {
            this.branchTypes.add((BranchType) type);
        } else {
            System.err.println("Unable to add type: " + type.name());
        }
    }

    public void build() throws Exception {
        this.createFieldType("text_type").setDataTypeName("TEXT");

        List<Exception> exceptions = new ArrayList<>();
        Consumer<Roles> leafResolver = role -> {
            for (LeafType leafType : this.leafTypes) {
                if (leafType.role().equals(role)) {
                    try {
                        leafType.resolve();
                    } catch (Exception ex) {
                        exceptions.add(ex);
                    }
                }
            }
        };
        leafResolver.accept(Roles.FieldType);
        leafResolver.accept(Roles.ValueType);

        Consumer<Roles> branchResolver = role -> {
            for (BranchType branchType : this.branchTypes) {
                if (branchType.role().equals(role)) {
                    try {
                        branchType.resolve();
                    } catch (Exception ex) {
                        exceptions.add(ex);
                    }
                }
            }
        };
        branchResolver.accept(Roles.Fragment);
        branchResolver.accept(Roles.BaseType);
        branchResolver.accept(Roles.ObjectType);

        for (Exception ex : exceptions) {
            System.err.println(ex.getMessage());
        }
    }

    public List<LeafType> getLeafTypes(Roles role) {
        return this.leafTypes.stream().filter(role).collect(Collectors.toList());
    }
    public List<BranchType> getBranchTypes(Roles role) {
        return this.branchTypes.stream().filter(role).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return FORMATTER.apply(this).replace("\t", "    ");
    }

    public Map metaMap() {
        return METAMAPPER.apply(this);
    }
}
