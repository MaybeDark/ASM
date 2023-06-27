//package org.exception;
//
//import org.InstructionSet;
//
//public class WrongWayException extends RuntimeException{
//    public WrongWayException() {
//        super();
//    }
//
//    public WrongWayException(String message){
//        super(message);
//    }
//
//    public static  WrongWayException build(InstructionSet instructionSet) {
//        StringBuilder message = new StringBuilder();
//        message.append("For this instruction, the wrong method was selected " +
//                "and the ");
//        message.append('\'');
//        if (instructionSet.isLoadFromConstantInstruction())
//            message.append("loadFromConstant");
//        else if (instructionSet.isLoadFromShortNumInstruction())
//            message.append("loadFromShortNum");
//        else if(instructionSet.isLoadFromConstantPoolInstruction())
//            message.append("loadFromConstantPool");
//        else if (instructionSet.isLoadFromLocalsInstruction())
//            message.append("loadFromLocals");
//        else if (instructionSet.isLoadFromArrayInstruction())
//            message.append("loadFromArray");
//        else if (instructionSet.isStoreToLocalsInstruction())
//            message.append("storeToLocals");
//        else if (instructionSet.isStoreToArrayInstruction())
//            message.append("storeToArray");
//        else if (instructionSet.isStackBehaviorInstruction())
//            message.append("stackBehavior");
//        else if (instructionSet.ismathOperandOperationInstruction())
//            message.append("operandOperation");
//        else if (instructionSet.isConvertInstruction())
//            message.append("operandConvert");
//        else if (instructionSet.isCompareInstruction())
//            message.append("operandCompare");
//        else if (instructionSet.isIfInstruction())
//            message.append("startIf");
//        else if (instructionSet.isGotoInstruction())
//            message.append("goto");
//        else if (instructionSet.isSwitchInstruction())
//            message.append("switch");
//        else if (instructionSet.isReturnInstruction())
//            message.append("setReturnType");
//        else if (instructionSet.isFieldInstruction())
//            message.append("loadOrSetField");
//        else if (instructionSet.isInvokeInstruction())
//            message.append("invokeMethod");
//        else{
//            message.append("specific");
//        }
//        message.append(" method should be used");
//        return new WrongWayException(message.toString());
//    }
//}
