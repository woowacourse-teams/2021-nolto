import '@testing-library/jest-dom';
import ReactDOM from 'react-dom';

import { server } from '__mocks__/msw/server';

beforeAll(() => {
  ReactDOM.createPortal = jest.fn((element, node) => {
    return element as React.ReactPortal;
  });
  server.listen();
});

afterEach(() => server.resetHandlers());

afterAll(() => server.close());

jest.spyOn(window, 'alert').mockImplementation((message) => console.log(message));
