package com.dongfeng.test;

import java.util.concurrent.CompletableFuture;

/**
 * @author eastFeng
 * @date 2020-12-12 14:14
 */
public class CFTest {
    public static void main(String[] args) {
        // 新建CompletableFuture对象
        CompletableFuture<String> cf = new CompletableFuture<>();

        // 可以选择在当前线程结束，也可以在其他线程结束
        cf.complete("coding...");

        try {
            // 获取结果
            String result = cf.get();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public synchronized void execute(long num){

    }


//    public List<StatisticsVo> statistics(Date start, Date end){
//        MatchOperation match = Aggregation.match(Criteria.where("createTime").gte(start).lt(end));
//        GroupOperation group1 = Aggregation.group("formId", "userId").count().as("renci");
//        GroupOperation group2 = Aggregation.group("_id.formId").count().as("renshu").sum("renci").as("renci");
//
//        Aggregation aggregation = Aggregation.newAggregation(match, group1, group2);
//
//        List<Map> results = mongoTemplate.aggregate(aggregation, "hymUserPunch", Map.class).getMappedResults();
//        List<StatisticsVo> list = new ArrayList<>(100);
//        for (Map map : results) {
//            StatisticsVo vo = new StatisticsVo();
//            try {
//                String formId = (String) map.get("_id");
//                Integer renshu = (Integer) map.get("renshu");
//                if (formId==null || "null".equals(formId)){
//                    continue;
//                }
//                vo.setFormId(formId);
//                vo.setTotal(renshu);
//
//                HymForm hymForm = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(formId)), HymForm.class);
//                if (hymForm!=null){
//                    vo.setPlaceName(hymForm.getPlaceName());
//                }
//            } catch (Exception e) {
//                log.error("statisticsError map:{},error:{}", JSON.toJSONString(map), e.getMessage(), e);
//            }
//            list.add(vo);
//        }
//
//        return list;
//    }
}
