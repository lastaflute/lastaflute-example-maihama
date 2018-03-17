import './profile';

route('/profile', () => {
  riot.mount('content', 'profile');
});

riot.mixin('profile', {
  api: {
    profile: '/profile',
  }
});