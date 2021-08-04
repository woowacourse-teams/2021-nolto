module.exports = {
  preset: 'ts-jest',
  setupFilesAfterEnv: ['./src/setUpTests.ts'],
  coveragePathIgnorePatterns: ['./node_modules/'],
  testEnvironment: 'jsdom',
  globals: {
    'ts-jest': {
      isolatedModules: true,
    },
  },
  moduleDirectories: ['./node_modules', 'src'],
  moduleNameMapper: {
    '\\.svg$': '<rootDir>/src/__mocks__/svgrMock.tsx',
  },
};
