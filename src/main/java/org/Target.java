//package org;
//
//public enum Target {
//    Class(1),
//    Method(2),
//    Field(4),
//    Parameter(8),
//    MF(6),
//    CMF(7),
//    CMFP(15),
//    LambdaMethod(16);
//
//    private final byte authority;
//    Target(byte authority){
//        this.authority = authority;
//    }
//
//    Target(int authority){
//        this((byte) authority);
//    }
//
//    public boolean checkTarget(Target target){
//        return (authority & target.authority) == target.authority;
//    }
//}
