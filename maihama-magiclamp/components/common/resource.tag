<resource>
  <script>
    window.RC = {};

    window.RC.ROUTE = {
      "root":             "",
      "product":    {
        "root":           "product",
        "list":           "productList",
        "detail":         "productDetail"
      },
      "member":           "member",
      "purchase":         "purchase",
      "various":          "various"
    };

    var url = "http://localhost:8092";
    window.RC.API = {
      "member": {
        "info":           url + "/hangar/member/info",
        "add":            url + "/data/member/add.json"
      },
      "mypage":         "http://localhost:8092/hangar/mypage",
      "product": {
        "list":           url + "/hangar/product/list/",
        "detail":         url + "/hangar/product/detail/"
      },
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
