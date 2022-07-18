package com.ljw.graph.util;

import com.google.common.collect.Lists;
import com.ljw.graph.entity.*;
import com.ljw.graph.entity.graph.Place;
import com.ljw.graph.entity.graph.Step;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class PecUtil {
    public static boolean containsTarget(List<Modifier> modifiers, String target) {
        for (Modifier modifier : modifiers) {
            if (StringUtils.equals(modifier.getNm1().getBase().getName(), target)) {
                return true;
            }

            if (StringUtils.equals(modifier.getNm3().getBase().getName(), target)) {
                return true;
            }
        }
        return false;
    }

    public static List<Modifier> containsTargetModifiers(List<Modifier> modifiers, String target) {
        List<Modifier> res = new ArrayList<>();
        for (Modifier modifier : modifiers) {
            if (StringUtils.equals(modifier.getNm1().getBase().getName(), target)) {
                res.add(modifier);
            } else if (StringUtils.equals(modifier.getNm3().getBase().getName(), target)) {
                res.add(modifier);
            }
        }

        return res;
    }

    public static List<NodeWithModifiers> modifiers修饰nm(NodeWithModifiers nm, List<Modifier> modifiers) {
        List<NodeWithModifiers> res = new ArrayList<>();
        for (Modifier modifier : modifiers) {
            NodeWithModifiers base = SerializationUtils.clone(nm);
            base.setModifiers(Lists.newArrayList(modifier));
        }
        return res;
    }


    public static List<NodeWithModifiers> modifiers的nm1或nm3被内层Nms修饰_然后整体修饰nm(List<Modifier> 需要修饰外层nm的modifiers, List<NodeWithModifiers> 需要修饰外层nm的modifiers_的内层Nms, NodeWithModifiers 外层nm) {
        List<NodeWithModifiers> res = new ArrayList<>();

        for (NodeWithModifiers 需要修饰外层nm的modifiers_的内层Nm : 需要修饰外层nm的modifiers_的内层Nms) {
            for (Modifier 需要修饰外层nm的modifier : 需要修饰外层nm的modifiers) {

                if (
                        !NodeWithModifiers.isEmpty(需要修饰外层nm的modifier.getNm1()) &&
                                StringUtils.equals(需要修饰外层nm的modifiers_的内层Nm.getBase().getName(), 需要修饰外层nm的modifier.getNm1().getBase().getName())
                ) {
                    需要修饰外层nm的modifier = SerializationUtils.clone(需要修饰外层nm的modifier);
                    需要修饰外层nm的modifier.setNm1(
                            SerializationUtils.clone(需要修饰外层nm的modifiers_的内层Nm)
                    );
                } else if (
                        !NodeWithModifiers.isEmpty(需要修饰外层nm的modifier.getNm3()) &&
                                StringUtils.equals(需要修饰外层nm的modifiers_的内层Nm.getBase().getName(), 需要修饰外层nm的modifier.getNm3().getBase().getName())
                ) {
                    需要修饰外层nm的modifier = SerializationUtils.clone(需要修饰外层nm的modifier);
                    需要修饰外层nm的modifier.setNm3(
                            SerializationUtils.clone(需要修饰外层nm的modifiers_的内层Nm)
                    );
                }


                NodeWithModifiers outNm = SerializationUtils.clone(外层nm);
                outNm.setModifiers(
                        Lists.newArrayList(需要修饰外层nm的modifier)
                );
                res.add(outNm);


            }


        }


        return res;

    }

    private static Map<String, HashMap<String, Set<Step>>> g;

    public static Map<String, HashMap<String, Set<Step>>> graphOfTriples() {
        if (g != null && g.size() > 0) {
            return g;
        }

        List<KgTriple> kgTriples = DataUtil.readKgTriplesWithAlias();

        g = new HashMap<>();

        for (KgTriple kgTriple : kgTriples) {
            Node element1 = kgTriple.getElement1();
            Node element2 = kgTriple.getElement2();
            Node element3 = kgTriple.getElement3();

//            if (element1.getName().equals("restaurant")) {
//                System.out.println("restaurant");
//            }
//            if (element3.getName().equals("some_shop_name")) {
//                System.out.println("some_shop_name");
//            }

            // 不要inquire
            if (element2.getName().equals("inquire")) {
                continue;
            }


            if (!g.containsKey(element1.getName())) {
                g.put(element1.getName(), new HashMap<>());
            }

            HashMap<String, Set<Step>> element1Neighbors = g.get(element1.getName());
            element1Neighbors.computeIfAbsent(element3.getName(), k -> new HashSet<>());
            element1Neighbors.get(element3.getName()).add(new Step(element3.getName(), element2.getName(), Place.RIGHT));


            if (!g.containsKey(element3.getName())) {
                g.put(element3.getName(), new HashMap<>());
            }

            HashMap<String, Set<Step>> element3Neighbors = g.get(element3.getName());
            element3Neighbors.computeIfAbsent(element1.getName(), k -> new HashSet<>());
            element3Neighbors.get(element1.getName()).add(new Step(element1.getName(), element2.getName(), Place.LEFT));

        }

        return g;
    }


    public static List<String> findActionReceiverNames(String act) {
        List<KgTriple> kgTriples = DataUtil.readKgTriplesWithAlias();

        HashSet<String> res = new HashSet<>();
        for (KgTriple kgTriple : kgTriples) {
            if (kgTriple.getElement2().getName().equals(act)) {
                res.add(kgTriple.getElement3().getName());
            }
        }

        return new ArrayList<>(res);
    }

    public static List<String> findActionSenderNames(String act) {
        List<KgTriple> kgTriples = DataUtil.readKgTriplesWithAlias();

        HashSet<String> res = new HashSet<>();
        for (KgTriple kgTriple : kgTriples) {
            if (kgTriple.getElement2().getName().equals(act)) {
                res.add(kgTriple.getElement1().getName());
            }
        }

        return new ArrayList<>(res);
    }




}
