# Plano de Polimento de Interação (Character Interaction Polish)

## Goal Description
O objetivo deste plano é polir a interação física e o combate entre os personagens (`player1` e `player2`) no jogo *Luta de Rua*, mantendo a atual arquitetura baseada em Swing e o Game Loop em `Game.java`. 

Atualmente, o jogo apresenta problemas clássicos de jogos de luta iniciais: o dano é aplicado continuamente se o botão ficar pressionado, os *hitboxes* são muito largos (causando "danos fantasma"), e os personagens podem atravessar um ao outro sem nenhuma resistência.

---

## User Review Required
> [!IMPORTANT]
> Foram identificados **3 pontos principais de melhoria**. Para cada um, sugeri 3 soluções possíveis e destaquei a minha **solução recomendada** que mantém a simplicidade e a estrutura atual do código.
> 
> Por favor, revise as soluções recomendadas abaixo. Se estiver de acordo, por favor aprove o plano para eu iniciar a codificação.

---

## 1. Aplicação de Dano Contínuo (Machine-gun Damage)
**Problema:** O laço principal roda a cada 20ms. Se o jogador mantiver a tecla de ataque (`J` ou `K`) pressionada, o dano é subtraído da vida do oponente 50 vezes por segundo, esgotando o HP quase instantaneamente.

**Possíveis Soluções:**
1. **(Recomendado) Controle de Estado (Cooldown de Ação):** Criar booleanos (ex: `p1HasAttacked`, `p1Kicked`) que garantam que um soco/chute aplique dano **apenas uma vez** por aperto de tecla. O dano só reseta quando a tecla for solta.
2. **Timers de Cooldown:** Adicionar um relógio (ex: `lastAttackTime`) que bloqueia a aplicação de dano caso o tempo desde o último ataque seja inferior a 500ms.
3. **Invulnerabilidade Pós-Dano (Hit-Stun):** Quando um personagem sofre dano, ele ganha uma tag `isInvincible` temporária de alguns frames.

## 2. Detecção de Colisão e Hitboxes Imprecisas
**Problema:** O jogo atual utiliza o `getBounds()` do `JLabel`, que inclui toda a área transparente (90x127). Personagens aplicam dano um no outro apenas por encostar nas "bordas invisíveis" das imagens.

**Possíveis Soluções:**
1. **(Recomendado) Shrink Hitbox (Caixas Reduzidas):** Adicionar um método customizado `getHitbox()` na classe `Player` que retorna um `Rectangle` ligeiramente menor e focado no centro do sprite (ex: margem de 20px de cada lado).
2. **Hitbox Baseado em Distância (Proximity):** Abandonar o `intersects()` e usar geometria simples: verificar se a distância absoluta entre os eixos centrais dos personagens `Math.abs(p1.centerX - p2.centerX)` é menor que a distância de alcance do soco.
3. **Hitboxes Assimétricas Dinâmicas:** Ter `Rectangle` separados para "Corpo" e "Ataque", onde a caixa de ataque só é projetada à frente do personagem no exato frame do soco.

## 3. Sobreposição de Personagens (Corpos Sólidos)
**Problema:** Na lógica de movimentação, o código simplesmente soma ou subtrai `speed` das coordenadas `x`. Os jogadores podem literalmente andar através do outro, sobrepondo os sprites por completo.

**Possíveis Soluções:**
1. **(Recomendado) Bloqueio de Movimento (Solid Body):** Antes de confirmar o deslocamento em `x += speed`, criar uma projeção do próximo movimento. Se essa projeção colidir com o oponente, o movimento é cancelado.
2. **Empurrão (Pushback on Hit):** Ao invés de travar o passo, permitir a movimentação, mas quando um ataque é conectado, empurrar fisicamente o defensor para trás (ex: `p2.x += 15`).
3. **Inversão Automática de Câmera/Lado:** Aceitar que eles se cruzem, mas programar o jogo para inverter instantaneamente os inputs e os sprites de P1 e P2 dependendo de quem estiver no lado esquerdo da tela.

---

## Proposed Changes (Implementação Recomendada)

### `src/modelo/Player.java`
Introduziremos as margens internas para criar *Hitboxes* mais fiéis ao visual dos personagens.

#### [MODIFY] src/modelo/Player.java
```java
    // NOVO: Retorna uma caixa de colisão menor que a imagem original
    public Rectangle getHitbox() {
        // Reduz a largura do hitbox em 40 pixels (20 de cada lado) 
        // e centraliza com base no x atual
        return new Rectangle(x + 20, y, 50, 127);
    }
```

### `src/controle/Game.java`
Implementaremos a lógica de bloqueio por estado (Edge Detection) e corpos sólidos usando o novo hitbox.

#### [MODIFY] src/controle/Game.java
```java
    // NOVOS ESTADOS para evitar dano contínuo
    Boolean p1HasAttacked = false;
    Boolean p2HasAttacked = false;

    // ...

    // MODIFICAÇÃO na checagem de colisão (usa Hitbox ao invés de Bounds)
    public void collision() {
        Rectangle rectangle1 = player1.getHitbox();
        Rectangle rectangle2 = player2.getHitbox();
        collision = rectangle1.intersects(rectangle2);
    }

    // ...
    
    // MODIFICAÇÃO na Movimentação P1 (Solid Body)
    if (keyD) {
        // Simula o próximo passo
        Rectangle nextStep = new Rectangle(player1.x + speed + 20, player1.y, 50, 127);
        if (player1.x <= 706 && !nextStep.intersects(player2.getHitbox())) {
            player1.setIconRight();
            player1.x += speed;
        }
    }
    
    // ...
    
    // MODIFICAÇÃO no Ataque P1 (Cooldown/Edge Detection)
    if (keyJ) {
        player1.setIconPunch();
        if (!p1HasAttacked) { // Só aplica dano no primeiro frame do aperto
            collision();
            if (collision) {
                // ... lógica de dano ...
            }
            p1HasAttacked = true; // Bloqueia danos subsequentes
        }
    } else if (keyK) {
        // ... Lógica similar para Chute ...
    } else {
        p1HasAttacked = false; // Reseta quando soltar a tecla
    }
```

## Verification Plan

### Automated Tests
* O projeto é compilado usando `javac -d bin $(find src -name "*.java")`. Se compilar corretamente, a sintaxe está íntegra.

### Manual Verification
O usuário deve executar a janela do jogo e testar o seguinte:
1. **Andar contra o oponente:** Tente cruzar o corpo do inimigo. O movimento deve ser bloqueado ao se tocarem.
2. **Distância de soco:** Teste a colisão tentando bater de muito perto e um pouco de longe para sentir se a *hitbox* está mais precisa ao sprite visual.
3. **Segurar botão de ataque:** Segure o botão de soco. A vida do inimigo deve descer apenas 1 vez. O botão deverá ser solto e apertado de novo para um novo soco dar dano.
