package Config;

/**
 * Created by Aman on 02-06-2016.
 */
public class Config {
    public static final String LINK_JSON = "http://52.76.28.14/Android/download.php?STORE_ID=";
    // public static final String Link_Download = "http://52.76.28.14/Android/json.php";
    public static final String Link_UPLOAD = "http://52.76.28.14/Android/2.php";


    public static final String Link_Update = "http://52.76.28.14/Android/update.php";
    public static final String Link_Download = "http://52.76.28.14/Android/json.php";
    public static final String Link_Upload = "http://52.76.28.14/Android/retail_videodata.php";
    public static final String Link_Upload_Media_Click = "http://52.76.28.14/Android/retail_media_click.php";


    /*****************************************URL For UPLOADS************************************************************/
    public static final String UPDATE_BILL_LEVEL = "http://52.76.28.14/systemsettings/bill_lvl_disc.php";
    public static final String UPDATE_DOCTOR = "http://52.76.28.14/Android/dr.discription.php";
    public static final String UPDATE_INVENTORY_WITHOUT_PO = "http://52.76.28.14/Android/inventory_without_po.php";
    public static final String UPDATE_INVENTORY_WITH_PO = "http://52.76.28.14/Android/inv.php";
    public static final String UPDATE_LINE_ITEM_DISCOUNT = "http://52.76.28.14/systemsettings/line_item_discount.php";
    public static final String UPDATE_TICKET_GENERATION = "http://52.76.28.14/systemsettings/ticket_generation.php";
    public static final String SALES_RETURN_WITHOUT_INVOICE_NO = "http://52.76.28.14/Android/sales_return_without_invoice.php";
    public static final String UPDATE_TOP_PRODUCT = "http://52.76.28.14/systemsettings/top_products.php";
    public static final String UPDATE_CUSTOMER = "http://52.76.28.14/systemsettings/top_products.php";
    public static final String DAY_OPEN = "http://52.76.28.14/Android/Day_open.php";
    public static final String UPDATE_LOCAL_PRODUCT = "http://52.76.28.14/Android/retail_store_prod_local.php";
    public static final String UPDATE_LOCAL_VENDOR = "http://52.76.28.14/Android/try.php";
    public static final String UPDATE_LOYALTY_HEAD = "http://52.76.28.14/systemsettings/loyality_ahead.php";
    public static final String UPDATE_RETAIL_CUST_LOYALTY = "http://52.76.28.14/systemsettings/retail_cust_loyality.php";
    public static final String UPDATE_NETWORKING_ISSUE = "http://52.76.28.14/MaintenenceIssue/NetworkingIssue.php";
    public static final String UPDATE_PRODUCT = "http://52.76.28.14/Android/retail_store_prod.php";
    public static final String SALES_BILL = "http://52.76.28.14/Android/pay_by_cash.php";
    public static final String SALES_MASTER_RETURN = "http://52.76.28.14/Android/retail_str_sales_master_return.php";
    public static final String UPDATE_STORE = "http://52.76.28.14/Android/retail_store.php";


/*
/////////////////////////////////////////////////URL FOR Development Upload//////////////////////////////////////////////
public static final String UPDATE_BILL_LEVEL = "http://52.76.28.14/development/systemsettings/bill_lvl_disc.php";
    public static final String UPDATE_DOCTOR = "http://52.76.28.14/development/Android/dr.discription.php";
    public static final String UPDATE_INVENTORY_WITHOUT_PO = "http://52.76.28.14/development/Android/inventory_without_po.php";
    public static final String UPDATE_INVENTORY_WITH_PO = "http://52.76.28.14/development/Android/inv.php";
    public static final String UPDATE_LINE_ITEM_DISCOUNT = "http://52.76.28.14/development/systemsettings/line_item_discount.php";
    public static final String UPDATE_TICKET_GENERATION= "http://52.76.28.14/development/systemsettings/ticket_generation.php";
    public static final String SALES_RETURN_WITHOUT_INVOICE_NO = "http://52.76.28.14/development/Android/sales_return_without_invoice.php";
    public static final String UPDATE_TOP_PRODUCT =  "http://52.76.28.14/development/systemsettings/top_products.php";
    public static final String UPDATE_CUSTOMER = "http://52.76.28.14/development/systemsettings/top_products.php";
    public static final String DAY_OPEN ="http://52.76.28.14/development/Android/Day_open.php";
    public static final String UPDATE_LOCAL_PRODUCT ="http://52.76.28.14/development/Android/retail_store_prod_local.php";
    public static final String UPDATE_LOCAL_VENDOR = "http://52.76.28.14/development/Android/try.php";
    public static final String UPDATE_LOYALTY_HEAD = "http://52.76.28.14/development/systemsettings/loyality_ahead.php";
    public static final String UPDATE_RETAIL_CUST_LOYALTY = "http://52.76.28.14/development/systemsettings/retail_cust_loyality.php";
    public static final String UPDATE_NETWORKING_ISSUE = "http://52.76.28.14/development/MaintenenceIssue/NetworkingIssue.php";
    public static final String UPDATE_PRODUCT = "http://52.76.28.14/development/Android/retail_store_prod.php";
    public static final String SALES_BILL = "http://52.76.28.14/development/Android/pay_by_cash.php";
    public static final String SALES_MASTER_RETURN = "http://52.76.28.14/development/Android/retail_str_sales_master_return.php";
    public static final String UPDATE_STORE = "http://52.76.28.14/development/Android/retail_store.php";
*/
    /***************************************RETAIL MEDIA CLICK DATA INSERT DATA INTO DATABASE***********************************************/

    public static final String VIDEODATA_ADPLAY = "ad_play";
    public static final String VIDEODATA_STOREID = "store_id";
    public static final String VIDEODATA_MEDIAID = "store_media_id";
    public static final String VIDEODATA_VIDEONAME = "video_name";
    public static final String VIDEODATA_STARTDATE = "start_date";
    public static final String VIDEODATA_ENDDATE = "end_date";
    public static final String VIDEODATA_STARTTIME = "start_time";
    public static final String VIDEODATA_ENDTIME = "end_time";


    /**************************
     * *************RETAIL VIDEO DATA INSERT DATA INTO DATABASE***********************************************/

    public static final String MEDIACLICK_ADPLAYID = "ad_play";
    public static final String MEDIACLICK_STOREMEDIAID = "media_store_id";
    public static final String MEDIACLICK_NUMBEROFCLICK = "number_of_click";
    public static final String MEDIACLICK_MOBILENUMBER = "mobile_number";

//==========================================JSON TAGS FOR TABLE NAME=====================================================================//


    String DATA;


    public static final String TAG_ARRAY_TABLE_FOUR = "retail_ad_store_main";

    public static final String TAG_ARRAY_TABLE_SIX = "retail_ad_ticker";

    public static final String TAG_ARRAY_TABLE_ELEVEN = "retail_cust";

    public static final String TAG_ARRAY_TABLE_FOURTEEN = "retail_media";
    public static final String TAG_ARRAY_TABLE_SIXTEEN = "retail_store";
    //public static final String TAG_ARRAY_TABLE_SIXTEEN = "retail_store";
    public static final String TAG_ARRAY_TABLE_37 = "retail_videodata";
    public static final String TAG_ARRAY_TABLE_38 = "retail_videodata_cont";
    public static final String TAG_ARRAY_TABLE_39 = "retail_videodata_cont1" +
            "";
    public static final String TAG_ARRAY_TABLE_42 = "ad_ticker_main";

    public static final String TAG_ARRAY_TABLE_59 = "ad_main";

    //intoduced on 5 March 2018.............................

    public static final String TAG_ARRAY_TABLE_60 = "retail_media_click";


    public static final String TAG_FIELD = "Field";

    @Override
    public String toString() {
        return DATA;

    }
    //*********************************************inventorywithoutpo****************************************************
    //   public static final String TAG_DATA="data";


//*******************Key For dr_discription*************************

    public static final String DOCTOR_DISCRIPTION_STORE_ID = "dr_store_id";
    public static final String DOCTOR_DISCRIPTION_ID = "dr_id";

    public static final String DOCTOR_DISCRIPTION_NAME = "dr_name";
    public static final String DOCTOR_DISCRIPTION_ADDRESS = "dr_address";
    public static final String DOCTOR_DISCRIPTION_SPECIALITY = "dr_speciality";


    ///***********Keys For Update Retail_Store***********************/////
    public static final String RETAIL_STORE_MOBILE_NUMBER = "mobile_no";
    public static final String RETAIL_STORE_FOOTER = "Footer";
    public static final String RETAIL_STORE_ALTERMOBILENO = "Tele_1";
    public static final String RETAIL_STORE_ALTERMOBILENO2 = "Tele_2";
    public static final String RETAIL_STORE_MRP = "MRP";


    public static final String RETAIL_STORE_STORE_ID = "store_id";


    //.............key for Update Retail_Store_Prod***************//
    public static final String RETAIL_STORE_PROD_STORE_ID = "store_id";
    public static final String RETAIL_STORE_PROD_SELLING_PRICE = "selling_price";
    public static final String RETAIL_STORE_PROD_PURCHASE_PRICE = "purchase_price";
    public static final String RETAIL_STORE_PROD_ACTIVE = "active";
    public static final String RETAIL_STORE_PROD_ID = "Prod_Id";


    //********** key for Update Retail_str_dstr**********************
    public static final String RETAIL_STR_DSTR_STORE_ID = "store_id";
    public static final String RETAIL_STR_DSTR_ACTIVE = "active";
    public static final String RETAIL_STR_DSTR_ID = "Dstr_Id";


    ///***************************************Keys For Local Vendor****************************************************************//

    public static final String LOCAL_VENDOR_STORE_ID = "store_id";
    public static final String LOCAL_VENDOR_ID = "vendor_id";
    public static final String LOCAL_VENDOR_NAME = "vendor_name";
    public static final String LOCAL_VENDOR_ADDRESS = "vendor_address";
    public static final String LOCAL_VENDOR_TELE = "vendor_tele";
    public static final String LOCAL_VENDOR_ZIP = "vendor_zip";
    public static final String LOCAL_VENDOR_CONTACT = "vendor_contact_name";
    public static final String LOCAL_VENDOR_MOBILE = "vendor_mobile";
    public static final String LOCAL_VENDOR_CITY = "vendor_city";
    public static final String LOCAL_VENDOR_COUNTRY = "vendor_country";
    public static final String LOCAL_VENDOR_INVENTORY = "vendor_inventory";
    public static final String LOCAL_VENDOR_EMAIL = "vendor_email";
    public static final String LOCAL_VENDOR_ACTIVE = "vendor_active";
    public static final String LOCAL_VENDOR_LAST_UPDATE = "last_update";
    public static final String LOCAL_VENDOR_S_Flag = "s_flag";

    /*******************************************************for vendor Payment******************************************/
    public static final String Retail_VendorPayment_store_id = "Store_Id";
    public static final String Retail_VendorPayment_PayID = "Payment_Id";
    public static final String Retail_VendorPayment_PurchaseOrderNo = "Pay_Id";
    public static final String Retail_VendorPayment_Amount = "Amount";
    public static final String Retail_VendorPayment_Vendorname = "Vend_Dstr_Nm";
    public static final String Retail_VendorPayment_Bank_Name = "Bank_Name";
    public static final String Retail_VendorPayment_Cheque_No = "Cheque_No";
    public static final String Retail_VendorPayment_Received_Amount = "Received_Amount";
    public static final String Retail_VendorPayment_Due_Amount = "Due_Amount";
    public static final String Retail_VendorPayment_Universal_id = "Universal_Id";
    public static final String Retail_VendorPayment_Flag = "Flag";
    public static final String Retail_VendorPayment_Reason = "Reason_Of_Pay";
    public static final String Retail_VendorPayment_payment_date = "Payment_Date";
    public static final String Retail_VendorPayment_prefix = "Prefix_Nm";


//***************************88maintainence****************************************************8

    public static final String Retail_Maintain_store_id = "Store_Id";
    public static final String Retail_Maintain_ticketid = "Ticket_Id";
    public static final String Retail_Maintain_ticket_support = "Support_Ticket_Status";
    public static final String Retail_Maintain_Subject_desc = "Subject_Desc";
    public static final String Retail_Maintain_Support_priority = "Support_Priority";
    public static final String Retail_Maintain_Teammember = "Team_Member";
    public static final String Retail_Maintain_Teamgroup = "Team_Group";
    public static final String Retail_Maintain_Comment = "Comment";
    public static final String Retail_Maintain_Date = "Date";
//=============Vendor_Return============================//

    public static final String RETAIL_STR_VENDOR_STORE_ID = "Store_Id";
    public static final String RETAIL_STR_VENDOR_RETURN_ID = "Vendor_Return_Id";
    public static final String RETAIL_STR_VENDOR_NAME = "Vendor_Nm";
    public static final String RETAIL_STR_REASON_RETURN = "Reason_Of_Return";
    public static final String RETAIL_STR_RETURN_AMOUNT = "Return_Amount";

    public static final String RETAIL_STR_PROD_NAME = "Prod_Nm";
    public static final String RETAIL_STR_EXP_DATE = "Exp_Date";
    public static final String RETAIL_STR_PURCHASE_PRICE = "P_Price";
    public static final String RETAIL_STR_BATCH_NO = "Batch_No";
    public static final String RETAIL_STR_UOM = "Uom";
    public static final String RETAIL_STR_QTY = "Qty";
    public static final String RETAIL_STR_TOTAL = "Total";
    public static final String RETAIL_CON_MUL_QTY = "Con_Mul_Qty";

//*************************************Fragmentdailysalesreport*****************************

    public static final String Retail_report_transid = "Tri_Id";
    public static final String Retail_report_grandtotal = "Total";
    public static final String Retail_report_uom = "Uom";
    public static final String Retail_report_productname = "Prod_Nm";
    public static final String Retail_report_expdate = "Exp_Date";
    public static final String Retail_report_sprice = "S_Price";
    public static final String Retail_report_batch = "Batch_No";

    public static final String Retail_report_Mrp = "Mrp";

    public static final String Retail_report_qty = "Qty";
    public static final String Retail_report_ticketid = "Ticket_Id";

    //*******************************************fragmentdistributorreport***********************
    public static final String Retail_report_active = "Active";
    public static final String Retail_report_dstrname = "Dstr_Nm";
    public static final String Retail_report_vendor_nm = "Vend_Nm";
    public static final String Retail_report_barcode = "Bar_Code";
    public static final String Retail_report_p_price = "P_Price";
    public static final String Retail_report_mrp = "MRP";
    public static final String Retail_report_profitmargin = "Profit_Margin";

//***********************************************************************************************

    public static final String Retail_report_Batch = "Batch_No";


//************************************************************************************

    public static final String Retail_report_saledate = "Sale_Date";

///***************************************paybycash*************************************************


    public static final String Retail_report_amountpaid = "Amount";
    public static final String Retail_report_receivedamoumt = "Received_Amount";
    public static final String Retail_report_vendorname = "Vend_Dstr_Nm";
    public static final String Retail_report_reportdueamount = "Due_Amount";
    public static final String Retail_report_reportreasonofpay = "Reason_Of_Pay";
    public static final String Retail_report_reportlastmodified = "Last_Modified";

//***************************************************cheque*********************************


    public static final String Retail_report_bankname = "Amount";
    public static final String Retail_report_chequeno = "Received_Amount";

    //****************************************purchasing**********************************************
    public static final String Retail_report_pono = "Po_No";
    public static final String Retail_report_vendornamepurchasing = "Vendor_Nm";
    public static final String Retail_report_total = "Total";
//*************************************************8blinkinglogo******************************************8

    public static final String Retail_report_mainid = "Ad_Main_Id";
    public static final String Retail_report_addesc = "Ad_Desc";
    public static final String Retail_report_Slb1 = "Ad_Cst_Slb1";
    public static final String Retail_report_Slb2 = "Ad_Cst_Slb2";
    public static final String Retail_report_Slb3 = "Ad_Cst_Slb3";
    public static final String Retail_report_startdate = "Ad_strt_Dt";
    public static final String Retail_report_enddate = "Ad_End_Dt";


    //===================Fragment_salesreturn=============================//


    public static final String FRAGMENT_POS_USER = "Pos_User";
    public static final String FRAGMENT_TRI_ID = "Tri_Id";
    public static final String FRAGMENT_TOTAL = "Total";
    public static final String FRAGMENT_PROD_NM = "Prod_Nm";
    public static final String FRAGMENT_QTY = "Qty";
    public static final String FRAGMENT_SALE_DATE = "Sale_Date";
    public static final String FRAGMENT_S_PRICE = "S_Price";
    public static final String FRAGMENT_EXP_DATE = "Exp_Date";
    public static final String FRAGMENT_UOM = "Uom";
    public static final String FRAGMENT_P_PRICE = "P_Price";

    /********************retail media click************************/

    public static final String RETAIL_MEDIACLICK_CUSTOMERNAME = "CUSTOMER_NM";
    public static final String RETAIL_MEDIACLICK_MOBILENUMBER = "mobile_number";
    public static final String RETAIL_MEDIACLICK_ADPLAY = "ad_play";
    public static final String RETAIL_MEDIACLICK_MEDIASTOREID = "media_store_id";
    public static final String RETAIL_MEDIACLICK_ADPLAY_NAME = "ad_play_name";
    public static final String RETAIL_MEDIACLICK_S_FLAG = "s_flag";
    public static final String RETAIL_MEDIACLICK_M_FLAG = "m_flag";
    public static final String RETAIL_MEDIACLICK_VIDEO_MAIL_FLAG = "videomail";


    /********************retail video data************************/

    public static final String RETAIL_VIDEODATA_AD_PLAY = "ad_play";
    public static final String RETAIL_VIDEODATA_FILE_NAME = "file_name";
    public static final String RETAIL_VIDEODATA_STORE_ID = "store_id";
    public static final String RETAIL_VIDEODATA_MEDIA_ID = "store_media_id";

    public static final String RETAIL_VIDEODATA_START_DATE = "start_date";
    public static final String RETAIL_VIDEODATA_END_DATE = "end_date";

    public static final String RETAIL_VIDEODATA_START_TIME = "start_time";
    public static final String RETAIL_VIDEODATA_END_TIME = "end_time";


    public static final String RETAIL_STORE_MRP_DECIMAL = "MRP_Decimal";
    public static final String RETAIL_STORE_PPRICE = "P_Price_Decimal";
    public static final String RETAIL_STORE_SPRICE = "S_Price_Decimal";
    public static final String RETAIL_STORE_HOLDPO = "Hold_Po";
    public static final String RETAIL_STORE_HOLDINV = "Hold_Inv";
    public static final String RETAIL_STORE_HOLDSALES = "Hold_Sales";

//***************************************************************retail_bill_display*********************

    public static final String RETAIL_STORE_TOTALBILLVALUE = "TOTAL_BILL_VALUE";
    public static final String RETAIL_STORE_DISCOUNT = "DISCOUNT";
    public static final String RETAIL_STORE_NETPAYABLE = "NET_BILL_PAYABLE";
    public static final String RETAIL_STORE_AMOUNTRECEIVED = "AMOUNT_RECEIVED";
    public static final String RETAIL_STORE_AMOUNTPAIDBACK = "AMOUNT_PAID_BACK";


}