import styled from 'styled-components';

const Root = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: ${({ theme }) =>
    `linear-gradient(90deg, ${theme.headerStartColor}, ${theme.headerEndColor})`};
  width: 100%;
  height: 250px;
  gap: 30px;
  margin-top: 50px;
  transition: background 0.3s ease;

  * {
    color: ${({ theme }) => theme.highLightedText};
  }
`;

export default { Root };
