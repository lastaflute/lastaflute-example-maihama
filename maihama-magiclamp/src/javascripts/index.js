require('../components/common/app')
require('../components/common/common-header')
require('../components/common/common-footer')
require('../components/common/controller')
require('../components/common/helper')
require('../components/common/pagenation')
require('../components/common/resource')
require('../components/common/title-reactive')

require('../components/member/member-add')
require('../components/member/member-edit')
require('../components/member/member-list')

require('../components/product/product-list')
require('../components/product/product-detail')

require('../components/profile/profile')

require('../components/root/root')
require('../components/root/signin')
require('../components/root/signup')

require('../stylesheets/reset.css')
require('../stylesheets/common.css')
require('../stylesheets/individual.css')

riot.mount('*')