
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

route.base('/');
route.start(true);

observable.on(RC.EVENT.route.change, (href) => {
  history.pushState(null, null, href);
  route(href);
});