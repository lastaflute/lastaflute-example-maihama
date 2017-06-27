route.base('/');

route('', () => {
  riot.mount('content', 'root');
});

route('/product/list/back', () => {
  riot.mount('content', 'product-list', { back: true });
});
route('/product/list..', () => {
  riot.mount('content', 'product-list', route.query());
});
route('/product/detail/*', (productId) => {
  riot.mount('content', 'product-detail', { productId });
});

route('/member/list/back', () => {
  riot.mount('content', 'member-list', { back: true });
});
route('/member/list..', () => {
  riot.mount('content', 'member-list', route.query());
});
route('/member/edit/*', (memberId) => {
  riot.mount('content', 'member-edit', { memberId });
});
route('/member/add', () => {
  riot.mount('content', 'member-add');
});
route('/member/purchase/*..', (memberId) => {
  console.log(route.query());
  console.log(memberId);
  const params = route.query();
  params.memberId = memberId;
  riot.mount('content', 'member-purchase-list', params);
});

route('/profile', () => {
  riot.mount('content', 'profile');
});

route('/signup', () => {
  riot.mount('content', 'signup');
});

route.start(true);

observable.on(RC.EVENT.route.change, (href) => {
  history.pushState(null, null, href);
  route(href);
});