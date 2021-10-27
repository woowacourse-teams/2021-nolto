import styled from 'styled-components';

const Root = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: ${({ theme }) =>
    `linear-gradient(90deg, ${theme.headerStartColor}, ${theme.headerEndColor})`};
  width: 100%;
  height: 248px;
  gap: 32px;
  margin-top: 48px;
  transition: background 0.3s ease;

  * {
    color: ${({ theme }) => theme.highLightedText};
  }
`;

export default { Root };
