package org.docksidestage.app.web.wx.remogen;

import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.wx.remogen.bean.routing.RoutingCheckForm;
import org.docksidestage.app.web.wx.remogen.bean.routing.RoutingCheckResult;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
@AllowAnyoneAccess
public class WxRemogenRoutingAction extends ShowbaseBaseAction {

    // *refers to WxRoutingAction of fortress project
    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/1
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/
    @Execute
    public JsonResponse<RoutingCheckResult> index(Integer first) {
        return asJson(new RoutingCheckResult("index()", "specified: " + first, null));
    }

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/maihama
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/
    // http://localhost:8098/showbase/wx/remogen/routing/maihama/dockside
    @Execute
    public JsonResponse<RoutingCheckResult> maihama() {
        return asJson(new RoutingCheckResult("maihama()", null, null));
    }

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/sea/dockside
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/sea/dockside/hangar
    @Execute
    public JsonResponse<RoutingCheckResult> sea(String first) {
        return asJson(new RoutingCheckResult("sea()", first, null));
    }

    // Cannot define overload method of action execute
    //@Execute
    //public JsonResponse<RoutingCheckResult> sea(String first, String second) {
    //    return asJson(new RoutingCheckResult("sea()", first, second));
    //}

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/land/dockside/hangar
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/land/dockside
    // http://localhost:8098/showbase/wx/remogen/routing/land/dockside/hangar/magiclamp
    @Execute
    public JsonResponse<RoutingCheckResult> land(String first, String second) {
        return asJson(new RoutingCheckResult("land()", first, second));
    }

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/piari
    // http://localhost:8098/showbase/wx/remogen/routing/piari/dockside
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/piari/dockside/hangar
    @Execute
    public JsonResponse<RoutingCheckResult> piari(OptionalThing<String> first) {
        return asJson(new RoutingCheckResult("piari()", first.orElse("*first"), null));
    }

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/dstore
    // http://localhost:8098/showbase/wx/remogen/routing/dstore/dockside
    // http://localhost:8098/showbase/wx/remogen/routing/dstore/dockside/hangar
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/dstore/dockside/hangar/magiclamp
    @Execute
    public JsonResponse<RoutingCheckResult> dstore(OptionalThing<String> first, OptionalThing<String> second) {
        return asJson(new RoutingCheckResult("dstore()", first.orElse("*first"), second.orElse("*second")));
    }

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/bonvo/dockside
    // http://localhost:8098/showbase/wx/remogen/routing/bonvo/dockside/hangar
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/bonvo
    // http://localhost:8098/showbase/wx/remogen/routing/bonvo/dockside/hangar/magiclamp
    @Execute
    public JsonResponse<RoutingCheckResult> bonvo(String first, OptionalThing<String> second) {
        return asJson(new RoutingCheckResult("bonvo()", first, second.orElse("*second")));
    }

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/amba/dockside/hangar
    // http://localhost:8098/showbase/wx/remogen/routing/amba/dockside/hangar/magiclamp
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/amba/dockside
    // http://localhost:8098/showbase/wx/remogen/routing/amba/dockside/hangar/magiclamp/orleans
    @Execute
    public JsonResponse<RoutingCheckResult> amba(String first, String second, OptionalThing<String> third) {
        return asJson(new RoutingCheckResult("amba()", first, second + " :: " + third.orElse("*third")));
    }

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/miraco/dockside/hangar/magiclamp
    // http://localhost:8098/showbase/wx/remogen/routing/miraco/dockside/hangar/magiclamp/orleans
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/miraco/dockside
    // http://localhost:8098/showbase/wx/remogen/routing/miraco/dockside/hangar
    @Execute
    public JsonResponse<RoutingCheckResult> miraco(String first, String second, String third, OptionalThing<String> fourth) {
        return asJson(new RoutingCheckResult("miraco()", first, second + " :: " + third + " :: " + fourth.orElse("*fourth")));
    }

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/dohotel/dockside
    // http://localhost:8098/showbase/wx/remogen/routing/dohotel/dockside/hangar
    // http://localhost:8098/showbase/wx/remogen/routing/dohotel/dockside/hangar?param=magiclamp
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/dohotel/dockside/hangar/magiclamp
    @Execute
    public JsonResponse<RoutingCheckResult> dohotel(String first, OptionalThing<String> second, RoutingCheckForm form) {
        return asJson(new RoutingCheckResult("dohotel()", first, second.orElse("*second") + " :: " + form));
    }

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/celeb/1
    // http://localhost:8098/showbase/wx/remogen/routing/celeb/1/2
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/celeb/dockside
    // http://localhost:8098/showbase/wx/remogen/routing/celeb/1/2/3
    @Execute
    public JsonResponse<RoutingCheckResult> celeb(Integer first, OptionalThing<Long> second) {
        return asJson(new RoutingCheckResult("celeb()", first, second.orElse(-99999L)));
    }

    // [hit]
    // http://localhost:8098/showbase/wx/remogen/routing/amphi/1/theater
    // [not]
    // http://localhost:8098/showbase/wx/remogen/routing/amphi/1/2
    // http://localhost:8098/showbase/wx/remogen/routing/amphi/1/dockside
    @Execute(urlPattern = "@word/{}/@word")
    public JsonResponse<RoutingCheckResult> amphiTheater(Integer first) {
        return asJson(new RoutingCheckResult("amphiTheater()", String.valueOf(first), null));
    }
}
