package com.pro.bityard.manger;

import java.util.Observable;

public class ContractManger extends Observable {

    private ContractManger() {

    }

    private static ContractManger contractManger = null;

    public static ContractManger getInstance() {
        if (contractManger == null) {
            synchronized (ContractManger.class) {
                if (contractManger == null) {
                    contractManger = new ContractManger();
                }
            }

        }
        return contractManger;

    }

    public void postTag(Object data) {

        setChanged();
        notifyObservers(data);

    }


    public void tag() {
        postTag(true);


    }

    /**
     * 清理消息监听
     */
    public void clear() {
        deleteObservers();
        contractManger = null;


    }

}
