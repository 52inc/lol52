package com.ftinc.lolserv.util;

import com.ftinc.lolserv.data.model.ModelMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscriber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ftinc.lolserv.util.Select.Query.Type.AND;
import static com.ftinc.lolserv.util.Select.Query.Type.NONE;
import static com.ftinc.lolserv.util.Select.Query.Type.OR;

/**
 * SQL Select query statment builder class that provides an intuitive API for <br/>
 * constructing <b>SELECT</b> sql statments
 *
 * <p>Created by r0adkll on 5/14/15.</p>
 */
public class Select {
    private static final Logger LOG = LoggerFactory.getLogger(Select.class);

    private Connection conn;

    // Query segments
    private String[] fields;
    private String table;
    private List<Query> queries;
    private String orderBy;

    private Select(){
        queries = new ArrayList<>();
    }

    String buildQuery(){
        StringBuilder sb = new StringBuilder("SELECT ");

        if(fields == null || fields.length == 0){
            sb.append("* ");
        }else{
            String field = StringUtils.join(fields, ',');
            sb.append(field.concat(" "));
        }

        sb.append("FROM ");

        if(Utils.isEmpty(table)){
            throw new NullPointerException("Please use a valid table");
        }

        sb.append(table.concat(" "));

        if(!queries.isEmpty()){
            sb.append("WHERE ");

            for(Query query: queries){
                sb.append(query.toSql());
            }
        }

        if(!Utils.isEmpty(orderBy)){
            sb.append("ORDER BY ");
            sb.append(orderBy);
        }

        return sb.toString().trim();
    }



    public static <T extends ModelMap<T>> Builder with(Class<T> clazz, Connection connection){
        return new Builder<>(connection, clazz);
    }

    /**
     * SELECT Builder class
     */
    public static class Builder<T extends ModelMap<T>>{

        private Class<T> clazz;
        private Select query;

        public Builder(Connection connection, Class<T> clazz){
            this.clazz = clazz;
            query = new Select();
            query.conn = connection;
        }

        // SELECT fields
        public Builder fields(String... fields){
            query.fields = fields;
            return this;
        }

        // SELECT fields FROM table
        public Builder table(String table){
            query.table = table;
            return this;
        }

        // Select fields FROM table WHERE operation<>arg
        public Builder where(String operation, Object... args){
            query.queries.add(new Query(NONE, operation, args));
            return this;
        }

        // Select fields FROM table WHERE operation<>arg AND operation<>arg
        public Builder and(String operation, Object... args){
            query.queries.add(new Query(AND, operation, args));
            return this;
        }

        // Select fields FROM table WHERE operation<>arg AND operation<>arg OR operation<>arg
        public Builder or(String operation, Object... args){
            query.queries.add(new Query(OR, operation, args));
            return this;
        }

        // Select fields FROM table WHERE operation<>arg AND operation<>arg OR operation<>arg ORDER BY id DESC
        public Builder orderBy(String order){
            query.orderBy = order;
            return this;
        }

        public List<T> fetch(){
            String sql = query.buildQuery();

            LOG.info("Select: {}", sql);

            try(PreparedStatement stmt = query.conn.prepareStatement(sql)){
                try(ResultSet result = stmt.executeQuery()){

                    List<T> items = new ArrayList<>();
                    while(result.next()){
                        Map<String, Object> map = Utils.resultToMap(result);
                        T item = clazz.newInstance();
                        items.add(item.fromMap(map));
                    }

                    return items;
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public T fetchSingle(){
            String sql = query.buildQuery();

            LOG.info("Select: {}", sql);

            try(PreparedStatement stmt = query.conn.prepareStatement(sql)){
                try(ResultSet result = stmt.executeQuery()){

                    T item = null;
                    if(result.next()){
                        Map<String, Object> map = Utils.resultToMap(result);
                        item = clazz.newInstance();
                        item.fromMap(map);
                    }

                    return item;
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Observable<List<T>> observe(){
            return Observable.create(new Observable.OnSubscribe<List<T>>() {
                @Override
                public void call(Subscriber<? super List<T>> subscriber) {

                    String sql = query.buildQuery();

                    LOG.info("Select: {}", sql);

                    try(PreparedStatement stmt = query.conn.prepareStatement(sql)){
                        try(ResultSet result = stmt.executeQuery()){

                            List<T> items = new ArrayList<>();
                            while(result.next()){
                                Map<String, Object> map = Utils.resultToMap(result);
                                T item = clazz.newInstance();
                                items.add(item.fromMap(map));
                            }

                            subscriber.onNext(items);
                            subscriber.onCompleted();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        subscriber.onError(e);
                    }
                }
            });
        }

        public Observable<T> observeSingle(){
            return Observable.create(new Observable.OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    String sql = query.buildQuery();

                    LOG.info("Select: {}", sql);

                    try(PreparedStatement stmt = query.conn.prepareStatement(sql)){
                        try(ResultSet result = stmt.executeQuery()){

                            T item = null;
                            if(result.next()){
                                Map<String, Object> map = Utils.resultToMap(result);
                                item = clazz.newInstance();
                                item.fromMap(map);
                            }

                            subscriber.onNext(item);
                            subscriber.onCompleted();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        subscriber.onError(e);
                    }
                }
            });
        }

    }

    public static class Query{

        public enum Type{
            NONE(""),
            AND("AND "),
            OR("OR ");

            private final String value;

            Type(String val){
                value = val;
            }

            public String val(){
                return value;
            }
        }

        public Type type;
        public String operation;
        public Object[] args;

        public Query(Type type, String op, Object[] args){
            this.type = type;
            this.operation = op;
            this.args = args;
        }

        public String toSql(){
            String[] _args = toStringArray(args);
            for(String arg: _args){
                // find and replace the first '?' in the string
                operation = operation.replaceFirst("\\?", arg);
            }

            return String.format("%s%s ", type.val(), operation);
        }

        protected final String[] toStringArray(final Object[] array) {
            if (array == null) {
                return null;
            }
            final String[] transformedArray = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                transformedArray[i] = String.valueOf(array[i]);
            }
            return transformedArray;
        }

    }

}
