package com.ljw.graph.controller;

import com.ljw.graph.conf.Constant;
import com.ljw.graph.dao.PecategoryCreateDao;
import com.ljw.graph.entity.*;
import com.ljw.graph.service.PhysicalEventWithPecategoryCreateCreator;
import com.ljw.graph.service.ShortestBfsProcessor;
import com.ljw.graph.util.CsvHelper;
import com.ljw.graph.util.DataHelper;
import com.ljw.graph.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class GeneratePesByPecategoryCreateController {
    @Autowired
    private PecategoryCreateDao pecategoryCreateDao;

    @RequestMapping(value = "/generatePesByPecategoryCreate")
    public String generatePesByPecategoryCreate() {

        List<PecategoryCreate> pecategoryCreates = pecategoryCreateDao.selectAll();

        DataUtil.write(Constant.PES_BY_PECATEGORY_CREATE_OUTPUT_JSON_FILE, "", false);// 重置文件
        DataUtil.write(Constant.PES_BY_PECATEGORY_CREATE_OUTPUT_CSV_FILE, "id,PE,type,node1,node2,node3\n", false);// 重置文件

        List<PhysicalEventWithPecategoryCreate> peWithPeccsTotal = new ArrayList<>();
        for (PecategoryCreate pecategoryCreate : pecategoryCreates) {
            String target1 = pecategoryCreate.getNode1();
            String beginAct = pecategoryCreate.getNode2();
            String target3 = pecategoryCreate.getNode3();

            String pecategory = pecategoryCreate.getPecategory();
            Boolean objDirect = pecategoryCreate.getObjDirect();


            List<NodeWithModifiers> nm1s = PhysicalEventWithPecategoryCreateCreator.createTargetToAct的完整路径Nms(beginAct, target1);
            List<NodeWithModifiers> nm3s = PhysicalEventWithPecategoryCreateCreator.createActToTarget的完整路径Nms(beginAct, target3);


            List<PhysicalEventWithPecategoryCreate> peWithPeccs = new ArrayList<>();

            for (NodeWithModifiers nm1 : nm1s) {
                for (NodeWithModifiers nm3 : nm3s) {
                    NodeWithModifiers nm2 = new NodeWithModifiers(Node.of(beginAct));

                    PhysicalEventWithPecategoryCreate peWithPecc = new PhysicalEventWithPecategoryCreate(pecategoryCreate);
                    peWithPecc.setNm1(nm1);
                    peWithPecc.setNm2(nm2);
                    peWithPecc.setNm3(nm3);

                    peWithPecc.setPecategory(PECategory.valueOf(pecategory));
                    peWithPecc.setObj_direct(objDirect);

                    peWithPeccs.add(peWithPecc);


                }
            }

            peWithPeccsTotal.addAll(peWithPeccs);

        }


        DataUtil.write(Constant.PES_BY_PECATEGORY_CREATE_OUTPUT_JSON_FILE, DataHelper.toJson(peWithPeccsTotal), true);
        DataUtil.write(Constant.PES_BY_PECATEGORY_CREATE_OUTPUT_CSV_FILE, CsvHelper.toCsvString(peWithPeccsTotal), true);

        return "success";


    }


}
