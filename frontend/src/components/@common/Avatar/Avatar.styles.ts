import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  align-items: center;
  gap: 16px;
  width: 100%;
`;

const Image = styled.img`
  width: 48px;
  height: 48px;
  border-radius: 50%;
`;

const Nickname = styled.span`
  font-size: 24px;
`;

export default { Root, Image, Nickname };
