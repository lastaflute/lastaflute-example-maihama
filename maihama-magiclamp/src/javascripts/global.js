import route from 'riot-route';
import Request from './request';
import Helper from './helper';
import Validator from './validator';
import Resource from './resource';

global.route = route;
global.observable = riot.observable();
global.request = new Request();
global.helper = new Helper();
global.validator = new Validator();
global.RC = new Resource;