import '@testing-library/jest-dom';

import { server } from '__mocks__/msw/server';

beforeAll(() => server.listen());

afterEach(() => server.resetHandlers());

afterAll(() => server.close());

jest.spyOn(window, 'alert').mockImplementation((message) => console.log(message));
