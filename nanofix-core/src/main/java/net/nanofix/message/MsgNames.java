package net.nanofix.message;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Mark
 * Date: 13/04/12
 * Time: 11:24
 */
public class MsgNames {
    private static final Map<String,String> msgNames = new HashMap<String, String>(100);
    private static final String UNKNOWN = "Unknown";

    static {
        msgNames.put(MsgTypes.Heartbeat, "Heartbeat");
        msgNames.put(MsgTypes.Logon, "Logon");
        msgNames.put(MsgTypes.TestRequest, "TestRequest");
        msgNames.put(MsgTypes.ResendRequest, "ResendRequest");
        msgNames.put(MsgTypes.Reject, "Reject");
        msgNames.put(MsgTypes.SequenceReset, "SequenceReset");
        msgNames.put(MsgTypes.Logout, "Logout");
        msgNames.put(MsgTypes.BusinessMessageReject, "BusinessMessageReject");
        msgNames.put(MsgTypes.UserRequest, "UserRequest");
        msgNames.put(MsgTypes.UserResponse, "UserResponse");
        msgNames.put(MsgTypes.Advertisement, "Advertisement");
        msgNames.put(MsgTypes.IndicationOfInterest, "IndicationOfInterest");
        msgNames.put(MsgTypes.News, "News");
        msgNames.put(MsgTypes.Email, "Email");
        msgNames.put(MsgTypes.QuoteRequest, "QuoteRequest");
        msgNames.put(MsgTypes.QuoteResponse, "AJ");
        msgNames.put(MsgTypes.QuoteRequestReject, "AG");
        msgNames.put(MsgTypes.RFQRequest, "AH");
        msgNames.put(MsgTypes.Quote, "S");
        msgNames.put(MsgTypes.QuoteCancel, "Z");
        msgNames.put(MsgTypes.QuoteStatusRequest, "a");
        msgNames.put(MsgTypes.QuoteStatusReport, "AI");
        msgNames.put(MsgTypes.MassQuote, "i");
        msgNames.put(MsgTypes.MassQuoteAcknowledgement, "b");
        msgNames.put(MsgTypes.MarketDataRequest, "V");
        msgNames.put(MsgTypes.MarketDataSnapshotFullRefresh, "W");
        msgNames.put(MsgTypes.MarketDataIncrementalRefresh, "X");
        msgNames.put(MsgTypes.MarketDataRequestReject, "Y");
        msgNames.put(MsgTypes.SecurityDefinitionRequest, "c");
        msgNames.put(MsgTypes.SecurityDefinition, "d");
        msgNames.put(MsgTypes.SecurityTypeRequest, "v");
        msgNames.put(MsgTypes.SecurityTypes, "w");
        msgNames.put(MsgTypes.SecurityListRequest, "x");
        msgNames.put(MsgTypes.SecurityList, "y");
        msgNames.put(MsgTypes.DerivativeSecurityListRequest, "z");
        msgNames.put(MsgTypes.DerivativeSecurityList, "AA");
        msgNames.put(MsgTypes.SecurityStatusRequest, "e");
        msgNames.put(MsgTypes.SecurityStatus, "f");
        msgNames.put(MsgTypes.TradingSessionStatusRequest, "g");
        msgNames.put(MsgTypes.TradingSessionStatus, "h");
        msgNames.put(MsgTypes.NewOrderSingle, "NewOrderSingle");
        msgNames.put(MsgTypes.ExecutionReport, "ExecutionReport");
        msgNames.put(MsgTypes.DontKnowTrade, "DontKnowTrade");
        msgNames.put(MsgTypes.OrderCancelReplaceRequest, "G");
        msgNames.put(MsgTypes.OrderCancelRequest, "F");
        msgNames.put(MsgTypes.OrderCancelReject, "9");
        msgNames.put(MsgTypes.OrderStatusRequest, "H");
        msgNames.put(MsgTypes.OrderMassCancelRequest, "q");
        msgNames.put(MsgTypes.OrderMassCancelReport, "r");
        msgNames.put(MsgTypes.OrderMassStatusRequest, "AF");
        msgNames.put(MsgTypes.NewOrderCross, "s");
        msgNames.put(MsgTypes.CrossOrderCancelReplaceRequest, "t");
        msgNames.put(MsgTypes.CrossOrderCancelRequest, "u");
        msgNames.put(MsgTypes.NewOrderMultileg, "AB");
        msgNames.put(MsgTypes.MultilegOrderCancelReplaceRequest, "AC");
        msgNames.put(MsgTypes.BidRequest, "k");
        msgNames.put(MsgTypes.BidResponse, "l");
        msgNames.put(MsgTypes.NewOrderList, "E");
        msgNames.put(MsgTypes.ListStrikePrice, "m");
        msgNames.put(MsgTypes.ListStatus, "N");
        msgNames.put(MsgTypes.ListExecute, "L");
        msgNames.put(MsgTypes.ListCancelRequest, "K");
        msgNames.put(MsgTypes.ListStatusRequest, "M");
        msgNames.put(MsgTypes.AllocationInstruction, "J");
        msgNames.put(MsgTypes.AllocationInstructionAck, "P");
        msgNames.put(MsgTypes.AllocationReport, "AS");
        msgNames.put(MsgTypes.AllocationReportAck, "AT");
        msgNames.put(MsgTypes.Confirmation, "AK");
        msgNames.put(MsgTypes.ConfirmationAck, "AU");
        msgNames.put(MsgTypes.ConfirmationRequest, "BH");
        msgNames.put(MsgTypes.SettlementInstructions, "T");
        msgNames.put(MsgTypes.SettlementInstructionRequest, "AV");
        msgNames.put(MsgTypes.TradeCaptureReportRequest, "AD");
        msgNames.put(MsgTypes.TradeCaptureReportRequestAck, "AQ");
        msgNames.put(MsgTypes.TradeCaptureReport, "AE");
        msgNames.put(MsgTypes.TradeCaptureReportAck, "AR");
        msgNames.put(MsgTypes.RegistrationInstructions, "o");
        msgNames.put(MsgTypes.RegistrationInstructionsResponse, "p");
        msgNames.put(MsgTypes.PositionMaintenanceRequest, "AL");
        msgNames.put(MsgTypes.PositionMaintenanceReport, "AM");
        msgNames.put(MsgTypes.RequestForPositions, "AN");
        msgNames.put(MsgTypes.RequestForPositionsAck, "AO");
        msgNames.put(MsgTypes.PositionReport, "AP");
        msgNames.put(MsgTypes.AssignmentReport, "AW");
        msgNames.put(MsgTypes.CollateralRequest, "AX");
        msgNames.put(MsgTypes.CollateralAssignment, "AY");
        msgNames.put(MsgTypes.CollateralResponse, "AZ");
        msgNames.put(MsgTypes.CollateralReport, "BA");
        msgNames.put(MsgTypes.CollateralInquiry, "BB");
        msgNames.put(MsgTypes.NetworkStatusRequest, "BC");
        msgNames.put(MsgTypes.NetworkStatusResponse, "BD");
        msgNames.put(MsgTypes.CollateralInquiryAck, "BG");
    }

    public static String add(String msgType, String msgName) {
        return msgNames.put(msgType, msgName);
    }

    public static String remove(String msgType) {
        return msgNames.remove(msgType);
    }

    public static String get(String msgType) {
        return msgNames.containsKey(msgType) ? msgNames.get(msgType) : UNKNOWN;
    }

}
