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
    '\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$':
      '<rootDir>/src/__mocks__/fileMock.tsx',
  },
};
