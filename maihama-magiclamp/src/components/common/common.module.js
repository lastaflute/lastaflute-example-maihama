
import './app';
import './common-header';
import './common-footer';
import './pagenation';
import './title-reactive';

import './root';
import './signin';
import './signup';

route('', () => {
  riot.mount('content', 'root');
});

route('/signup', () => {
  riot.mount('content', 'signup');
});

riot.mixin('common', {
  api: {
    mypage: '/mypage',
    auth: {
      signin: '/signin',
      signup: '/signup',
      signout: '/signout'
    },
    member: {
      info: '/member/info'
    }
  }
});

route.base('/');
route.start(true);

observable.on(EVENT.route.change, (href) => {
  history.pushState(null, null, href);
  route(href);
});