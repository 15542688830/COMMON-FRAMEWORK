package com.keywordstone.framework.common.mongo.client;

import com.alibaba.fastjson.JSON;
import com.keywordstone.framework.common.basic.dto.PageResultDTO;
import com.keywordstone.framework.common.basic.entity.Pageable;
import com.keywordstone.framework.common.mongo.config.MongoConfig;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author k
 */
@Component
public class Mongo {

    private final static String ID = "id";

    private static MongoDatabase database = MongoConfig.getDatabase();

    public static <T> boolean insert(String table,
                                     T entity) {
        if (StringUtils.isNotEmpty(table)
                && null != entity) {
            BeanMap map = BeanMap.create(entity);
            if (!map.containsKey(ID)) {
                return false;
            }
            Document document = new Document();
            document.putAll(map);
            database.getCollection(table).insertOne(document);
            return true;
        }
        return false;
    }

    public static boolean deleteById(String table,
                                     String id) {
        if (StringUtils.isNoneEmpty(table, id)) {
            database.getCollection(table).deleteOne(Filters.eq(id));
            return true;
        }
        return false;
    }

    public static boolean deleteBy(String table,
                                   Bson filter) {
        if (StringUtils.isNotEmpty(table)
                && null != filter) {
            database.getCollection(table).deleteMany(filter);
            return true;
        }
        return false;
    }

    public static <T> boolean updateById(String table,
                                         T entity) {
        if (StringUtils.isNotEmpty(table)
                && null != entity) {
            BeanMap map = BeanMap.create(entity);
            if (!map.containsKey(ID)) {
                return false;
            }
            Document document = new Document();
            document.putAll(map);
            database.getCollection(table).replaceOne(Filters.eq(ID, map.get(ID)), document);
            return true;
        }
        return false;
    }

    public static <T> boolean updateBy(String table,
                                       Bson filter,
                                       T entity) {
        if (StringUtils.isNotEmpty(table)
                && null != filter
                && null != entity) {
            Document document = new Document();
            document.putAll(BeanMap.create(entity));
            database.getCollection(table).replaceOne(filter, document);
            return true;
        }
        return false;
    }

    public static <T> T findById(String table,
                                 String id,
                                 Class<T> clazz) {
        Document document = database.getCollection(table).find(Filters.eq(ID, id)).first();
        if (null != document) {
            return JSON.parseObject(document.toJson(), clazz);
        }
        return null;
    }

    public static <T> List<T> findBy(String table,
                                     Bson filter,
                                     Class<T> clazz) {

        PageResultDTO pageResultDTO = findPageableBy(table, filter, clazz, null);
        if (null != pageResultDTO) {
            return pageResultDTO.getResult();
        }
        return null;
    }

    public static <T> PageResultDTO<T> findPageableBy(String table,
                                                      Bson filter,
                                                      Class<T> clazz,
                                                      Pageable pageable) {
        long count = database.getCollection(table).count(filter);
        if (0 < count) {
            FindIterable<Document> documents = database.getCollection(table).find(filter);
            if (null != pageable && pageable.isPage()) {
                Integer pageNo = pageable.getPageNo();
                Integer pageSize = pageable.getPageSize();
                if (null == pageNo || 1 > pageNo) {
                    pageNo = 1;
                }
                if (null == pageSize || 1 > pageSize) {
                    pageSize = 10;
                }
                documents = documents.skip((pageNo - 1) * pageSize).limit(pageNo * pageSize);
            }
            List<T> list = new ArrayList<>();
            for (Iterator<Document> iterator = documents.iterator(); iterator.hasNext(); ) {
                list.add(JSON.parseObject(iterator.next().toJson(), clazz));
            }
            return PageResultDTO.result(count, list);
        }
        return PageResultDTO.result(0, null);
    }
}
