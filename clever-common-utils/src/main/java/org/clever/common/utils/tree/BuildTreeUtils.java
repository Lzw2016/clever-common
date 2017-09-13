package org.clever.common.utils.tree;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 构建对象树结构的工具类<br/>
 * <p/>
 * 作者：LiZW <br/>
 * 创建时间：2016-5-8 22:05 <br/>
 */
@Slf4j
public class BuildTreeUtils {
    /**
     * 构建树结构，可能有多棵树<br/>
     *
     * @param nodes 所有要构建树的节点
     * @return 构建的所有树的根节点
     */
    public static List<ITreeNode> buildTree(Collection<ITreeNode> nodes) {
        log.debug("开始构建树结构...");
        final long startTime = System.currentTimeMillis();
        // 需要构建树的节点，还未构建到树中的节点
        List<ITreeNode> allTreeNodeList = getCanBuildTreeNodes(nodes);
        // 清除构建状态
        clearBuild(allTreeNodeList);
        // 查找所有根节点
        List<ITreeNode> rootNodeList = findRootNode(allTreeNodeList);
        // 刷新还未构建到树中的节点，减少循环次数
        List<ITreeNode> noBuildTreeNodeList = refreshNoBuildNodes(allTreeNodeList);
        // 循环根节点，构建多棵树
        for (ITreeNode rootNode : rootNodeList) {
            // 递归生成树
            buildTree(rootNode, noBuildTreeNodeList);
            // 刷新还未构建到树中的节点，减少循环次数
            noBuildTreeNodeList = refreshNoBuildNodes(noBuildTreeNodeList);
        }
        final long endTime = System.currentTimeMillis();
        // 校验构建是否正确
        if (noBuildTreeNodeList.size() <= 0) {
            log.debug("树构建成功！耗时：" + (endTime - startTime) + "ms");
        } else {
            log.debug("树构建失败！耗时：" + (endTime - startTime) + "ms");
        }
        return rootNodeList;
    }

    /**
     * 刷新还未构建到树中的节点<br/>
     *
     * @param noBuildTreeNodeList 还未构建到树中的节点集合
     * @return 刷新后的还未构建到树中的节点集合
     */
    private static List<ITreeNode> refreshNoBuildNodes(List<ITreeNode> noBuildTreeNodeList) {
        List<ITreeNode> newNoBuildTreeNodeList = new ArrayList<>();
        for (ITreeNode node : noBuildTreeNodeList) {
            if (!node.isBuild()) {
                newNoBuildTreeNodeList.add(node);
            }
        }
        return newNoBuildTreeNodeList;
    }

    /**
     * 递归生成树<br/>
     *
     * @param parentNode          父节点
     * @param noBuildTreeNodeList 所有未被添加到父节点下的节点
     */
    private static void buildTree(ITreeNode parentNode, List<ITreeNode> noBuildTreeNodeList) {
        for (ITreeNode node : noBuildTreeNodeList) {
            if (!node.isBuild() && Objects.equals(node.getParentId(), parentNode.getId())) {
                // 设置已经被添加到父节点下了
                node.setBuild(true);
                parentNode.addChildren(node);

                // 递归生成树
                buildTree(node, noBuildTreeNodeList);
                // buildTree(node, refreshNoBuildNodes(noBuildTreeNodeList));
            }
        }
    }

    /**
     * 过滤节点对象，排除不能构建树的节点，不能构建树的节点满足以下条件：<br/>
     * 1.节点对象为null (node == null)<br/>
     * 2.节点ID为null (node.getId() == null)<br/>
     * 3.父节点ID为null (node.getParentId() == null)<br/>
     * 4.节点全路径为null (node.getFullPath() == null)<br/>
     *
     * @param nodes 所有要构建树的节点
     * @return 所有可以构建树的节点，即节点数据验证通过的节点
     */
    private static List<ITreeNode> getCanBuildTreeNodes(Collection<ITreeNode> nodes) {
        List<ITreeNode> treeNodeList = new ArrayList<>();
        // 初始化需要构建树的节点
        for (ITreeNode node : nodes) {
            if (node != null && node.getId() != null && node.getParentId() != null && node.getFullPath() != null) {
                treeNodeList.add(node);
            }
        }
        return treeNodeList;
    }

    /**
     * 清除节点的构建状态，以用于重新构建树<br/>
     *
     * @param noBuildTreeNodeList 所有要构建树的节点
     */
    private static void clearBuild(List<ITreeNode> noBuildTreeNodeList) {
        for (ITreeNode node : noBuildTreeNodeList) {
            node.setBuild(false);
        }
    }

    /**
     * 在节点中查找所有根节点，根节点满足以下条件：<br/>
     * 1.节点ID等于节点全路径<br/>
     * 2.节点的父节点ID等于-1<br/>
     * 3.在节点集合中找不到某个节点的父节点，那么这个节点就是根节点<br/>
     *
     * @param noBuildTreeNodeList 所有要构建树的节点
     * @return 所有根节点
     */
    private static List<ITreeNode> findRootNode(List<ITreeNode> noBuildTreeNodeList) {
        // 所有根节点
        List<ITreeNode> rootNodeList = new ArrayList<>();
        for (ITreeNode node : noBuildTreeNodeList) {
            // 节点全路径等于节点ID
            if (node.getId().toString().equals(node.getFullPath())) {
                rootNodeList.add(node);
                node.setBuild(true);
                continue;
            }
            // 节点的父节点ID等于-1
            if (node.getParentId() == -1L) {
                rootNodeList.add(node);
                node.setBuild(true);
                continue;
            }
            // 在节点集合中找不到某个节点的父节点，那么这个节点就是根节点
            boolean flag = true;// 当前节点(node)是否是根节点
            for (ITreeNode n : noBuildTreeNodeList) {
                if (!node.equals(n) && Objects.equals(node.getParentId(), n.getId())) {
                    flag = false;// 当前节点不是根节点
                    break;
                }
            }
            if (flag) {
                rootNodeList.add(node);
                node.setBuild(true);
            }
        }
        return rootNodeList;
    }
}
