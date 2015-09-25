/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.dbflute.cbean.cq.ciq;

import java.util.Map;
import org.dbflute.cbean.*;
import org.dbflute.cbean.ckey.*;
import org.dbflute.cbean.coption.ConditionOption;
import org.dbflute.cbean.cvalue.ConditionValue;
import org.dbflute.cbean.sqlclause.SqlClause;
import org.dbflute.exception.IllegalConditionBeanOperationException;
import org.docksidestage.dbflute.cbean.*;
import org.docksidestage.dbflute.cbean.cq.bs.*;
import org.docksidestage.dbflute.cbean.cq.*;

/**
 * The condition-query for in-line of purchase.
 * @author DBFlute(AutoGenerator)
 */
public class PurchaseCIQ extends AbstractBsPurchaseCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected BsPurchaseCQ _myCQ;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public PurchaseCIQ(ConditionQuery referrerQuery, SqlClause sqlClause
                        , String aliasName, int nestLevel, BsPurchaseCQ myCQ) {
        super(referrerQuery, sqlClause, aliasName, nestLevel);
        _myCQ = myCQ;
        _foreignPropertyName = _myCQ.xgetForeignPropertyName(); // accept foreign property name
        _relationPath = _myCQ.xgetRelationPath(); // accept relation path
        _inline = true;
    }

    // ===================================================================================
    //                                                             Override about Register
    //                                                             =======================
    @Override
    protected void reflectRelationOnUnionQuery(ConditionQuery bq, ConditionQuery uq)
    { throw new IllegalConditionBeanOperationException("InlineView cannot use Union: " + bq + " : " + uq); }

    @Override
    protected void setupConditionValueAndRegisterWhereClause(ConditionKey k, Object v, ConditionValue cv, String col)
    { regIQ(k, v, cv, col); }

    @Override
    protected void setupConditionValueAndRegisterWhereClause(ConditionKey k, Object v, ConditionValue cv, String col, ConditionOption op)
    { regIQ(k, v, cv, col, op); }

    @Override
    protected void registerWhereClause(String wc)
    { registerInlineWhereClause(wc); }

    @Override
    protected boolean isInScopeRelationSuppressLocalAliasName() {
        if (_onClause) { throw new IllegalConditionBeanOperationException("InScopeRelation on OnClause is unsupported."); }
        return true;
    }

    // ===================================================================================
    //                                                                Override about Query
    //                                                                ====================
    @Override
    protected ConditionValue xgetCValuePurchaseId() { return _myCQ.xdfgetPurchaseId(); }
    @Override
    public String keepPurchaseId_ExistsReferrer_PurchasePaymentList(PurchasePaymentCQ sq)
    { throwIICBOE("ExistsReferrer"); return null; }
    @Override
    public String keepPurchaseId_NotExistsReferrer_PurchasePaymentList(PurchasePaymentCQ sq)
    { throwIICBOE("NotExistsReferrer"); return null; }
    @Override
    public String keepPurchaseId_SpecifyDerivedReferrer_PurchasePaymentList(PurchasePaymentCQ sq)
    { throwIICBOE("(Specify)DerivedReferrer"); return null; }
    @Override
    public String keepPurchaseId_QueryDerivedReferrer_PurchasePaymentList(PurchasePaymentCQ sq)
    { throwIICBOE("(Query)DerivedReferrer"); return null; }
    @Override
    public String keepPurchaseId_QueryDerivedReferrer_PurchasePaymentListParameter(Object vl)
    { throwIICBOE("(Query)DerivedReferrer"); return null; }
    @Override
    protected ConditionValue xgetCValueMemberId() { return _myCQ.xdfgetMemberId(); }
    @Override
    protected ConditionValue xgetCValueProductId() { return _myCQ.xdfgetProductId(); }
    @Override
    protected ConditionValue xgetCValuePurchaseDatetime() { return _myCQ.xdfgetPurchaseDatetime(); }
    @Override
    protected ConditionValue xgetCValuePurchaseCount() { return _myCQ.xdfgetPurchaseCount(); }
    @Override
    protected ConditionValue xgetCValuePurchasePrice() { return _myCQ.xdfgetPurchasePrice(); }
    @Override
    protected ConditionValue xgetCValuePaymentCompleteFlg() { return _myCQ.xdfgetPaymentCompleteFlg(); }
    @Override
    protected ConditionValue xgetCValueRegisterDatetime() { return _myCQ.xdfgetRegisterDatetime(); }
    @Override
    protected ConditionValue xgetCValueRegisterUser() { return _myCQ.xdfgetRegisterUser(); }
    @Override
    protected ConditionValue xgetCValueUpdateDatetime() { return _myCQ.xdfgetUpdateDatetime(); }
    @Override
    protected ConditionValue xgetCValueUpdateUser() { return _myCQ.xdfgetUpdateUser(); }
    @Override
    protected ConditionValue xgetCValueVersionNo() { return _myCQ.xdfgetVersionNo(); }
    @Override
    protected Map<String, Object> xfindFixedConditionDynamicParameterMap(String pp) { return null; }
    @Override
    public String keepScalarCondition(PurchaseCQ sq)
    { throwIICBOE("ScalarCondition"); return null; }
    @Override
    public String keepSpecifyMyselfDerived(PurchaseCQ sq)
    { throwIICBOE("(Specify)MyselfDerived"); return null;}
    @Override
    public String keepQueryMyselfDerived(PurchaseCQ sq)
    { throwIICBOE("(Query)MyselfDerived"); return null;}
    @Override
    public String keepQueryMyselfDerivedParameter(Object vl)
    { throwIICBOE("(Query)MyselfDerived"); return null;}
    @Override
    public String keepMyselfExists(PurchaseCQ sq)
    { throwIICBOE("MyselfExists"); return null;}

    protected void throwIICBOE(String name)
    { throw new IllegalConditionBeanOperationException(name + " at InlineView is unsupported."); }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xinCB() { return PurchaseCB.class.getName(); }
    protected String xinCQ() { return PurchaseCQ.class.getName(); }
}
