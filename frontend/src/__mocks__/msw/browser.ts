import { setupWorker } from 'msw';

import { handlers } from './handlers';

//for storybook (browser)
export const worker = typeof global.process === 'undefined' && setupWorker(...handlers);
