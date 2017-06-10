<resource>
  <script>
    window.RC = {};

    var url = "http://localhost:8092";
    window.RC.API = {
      "member": {
        "info":           url + "/hangar/member/info",
        "status":         url + "/hangar/member/status/",
        "list":           url + "/hangar/member/list/",
        "detail":         url + "/hangar/member/edit/",
        "update":         url + "/hangar/member/edit/update/",
        "add":            url + "/hangar/member/add/register/"
      },
      "mypage":         "http://localhost:8092/hangar/mypage",
      "product": {
        "status":         url + "/hangar/product/list/status/",
        "list":           url + "/hangar/product/list/search/",
        "detail":         url + "/hangar/product/detail/"
      },
      "profile":          url + "/hangar/profile",
      "auth": {
        "signin":         url + "/hangar/signin",
        "signup":         url + "/data/auth/register.json",
        "signout":        url + "/hangar/signout"
      }
    };

    window.RC.EVENT = {
      "route": {
        "change":         "onRounteChange"
      },
      "auth": {
        "check":          "onCheckSignin",
        "sign":           "onSign"
      },
      "pagenation": {
        "set":            "onPagenationSet"
      }
    };

    window.RC.SESSION = {
      "member": {
        "info":           "memberInfo"
      }
    };
  </script>
</resource>
